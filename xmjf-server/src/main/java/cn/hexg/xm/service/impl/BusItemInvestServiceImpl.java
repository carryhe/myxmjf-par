package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.constant.ItemStatus;
import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.db.dao.*;
import cn.hexg.xm.dto.BusItemInvestDto;
import cn.hexg.xm.po.*;
import cn.hexg.xm.service.IBasUserSecurityService;
import cn.hexg.xm.service.IBasUserService;
import cn.hexg.xm.service.IBusItemInvestService;
import cn.hexg.xm.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class BusItemInvestServiceImpl extends BaseService<BusItemInvest> implements IBusItemInvestService {

    @Resource
    private IBasUserDao basUserDao;

    @Resource
    private IBasUserSecurityService basUserSecurityService;

    @Resource
    private BasItemDao basItemDao;

    @Resource
    private BusUserStatDao busUserStatDao;

    @Resource
    private BusItemInvestDao busItemInvestDao;

    @Resource
    private BusUserIntegralDao busUserIntegralDao;
    @Resource
    private BusAccountDao busAccountDao;

    @Resource
    private BusAccountLogDao busAccountLogDao;


    @Override
    public void addBusItemInvest(Integer id, BigDecimal amount, Integer itemId, String password) {
        /*
        * 1.参数校验
                用户登录校验
                交易密码校验
                投资项目记录存在校验
                项目是否移动端校验
                投资项目开放状态校验
                项目投资剩余金额合法校验
                投资金额合法校验
                单笔投资 是否大于(小于) 投资金额校验
                投资金额 大于剩余金额 取剩余金额计算(个人账户可用金额 合法>投资金额)
                新手标重复投资记录校验(用户投资状态记录表查询)
            2.执行投资
                使用表（
                bus_item_invest:项目投资表
                bus_user_stat :用户统计表
                bus_user_integral:用户积分表
                bus_account: 账户信息表
                bus_account_log :用户账户操作日志表
                ）
            2.1 添加投资记录
                设置投资表相关字段值
                注意:
                利率计算 使用工具类
            2.2 添加积分
                每进行一次积分 添加 100 积分
            2.3 更新统计表信息
             投资次数 金额
            2.4 更新账户信息表
                账户冻结金额设置 投资金额
                账户待收金额设置 原始待收+待收利息
                账户总金额
            2.5 账户操作日志信息更新
            3.回调处理 （省略）
            4.返回投资结果
        *
        * */
        checkParams(amount,password,itemId);
        
        AssertUtil.isTrue(basUserDao.queryById(id)==null,"用户不合法！");
        BasUserSecurity basUserSecurity = basUserSecurityService.queryUserSecurityByUserId(id);
        //进行校验交易密码
       // password= MD5.toMD5(password);
        AssertUtil.isTrue(!password.equals(basUserSecurity.getPaymentPassword()),"交易密码不正确！");
        BasItem basItem= basItemDao.queryById(itemId);
        AssertUtil.isTrue(null==basItem,"带投标的记录不存在!");
        //wenti
        AssertUtil.isTrue(basItem.getMoveVip().equals(1),"移动端项目，web不能进行投资操作!");
        AssertUtil.isTrue(!basItem.getItemStatus().equals(ItemStatus.OPEN),
                "该项目处于未开放状态，暂时不能进行投资操作!");
        //项目投资金额校验
        BigDecimal syAmount=basItem.getItemAccount().add(basItem.getItemOngoingAccount().negate());
        int result=syAmount.compareTo(BigDecimal.ZERO);
        AssertUtil.isTrue(result<=0,"项目已满标，不可进行投资操作!");

        BigDecimal singleMinInvestAmount=basItem.getItemSingleMinInvestment();
        if(singleMinInvestAmount.compareTo(BigDecimal.ZERO)>0){
            AssertUtil.isTrue(amount.compareTo(singleMinInvestAmount)<0,"投资金额小于单笔投资最小金额!");
        }
        BigDecimal singleMaxInvestAmount=basItem.getItemSingleMaxInvestment();
        if(singleMaxInvestAmount.compareTo(BigDecimal.ZERO)>0){
            //AssertUtil.isTrue(amount.compareTo(singleMaxInvestAmount));
            if(amount.compareTo(singleMaxInvestAmount)>0){
                amount=singleMaxInvestAmount;
            }
        }
        AssertUtil.isTrue(syAmount.compareTo(singleMinInvestAmount)<0,"项目处于截标阶段，不可进行投资操作!");
        //  如果项目剩余金额在最小投资与最大投资之间 投资金额大于剩余金额
        if(syAmount.compareTo(singleMinInvestAmount)>0&&syAmount.compareTo(singleMaxInvestAmount)<=0){
            if(amount.compareTo(syAmount)>0){
                amount=syAmount;
            }
        }
        int itemIsNew=(int)basItem.getItemIsnew();
        if(itemIsNew==1){
            BusUserStat busUserStat=busUserStatDao.queryBusUserStatByUserId(id);
            //问题
            AssertUtil.isTrue(busUserStat.getInvestCount()>0,"新手标不能重复投资!");
        }
        doInvest(amount,itemId,id,basItem);



    }

    @Override
    public Map<String, Object> queryItemInvestsFiveMonthByUserId(Integer userId) {
        List<BusItemInvestDto> busItemInvestDtos= busItemInvestDao.queryItemInvestsFiveMonthByUserId(userId);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("code",200);
        List<String> months=null;
        List<BigDecimal> investAmounts=null;
        months=new ArrayList<String>();
        investAmounts=new ArrayList<BigDecimal>();
        for(BusItemInvestDto busItemInvestDto:busItemInvestDtos){
            months.add(busItemInvestDto.getMonth());
            investAmounts.add(busItemInvestDto.getInvestAmount());
        }
        map.put("months",months);
        map.put("amounts",investAmounts);
        return map;
    }

    /**
     * 处理添加投资记录
     * @param amount
     * @param itemId
     * @param id
     * @param basItem
     */
    private void doInvest(BigDecimal amount, Integer itemId, Integer id, BasItem basItem) {
        BusItemInvest busItemInvest=new BusItemInvest();
        busItemInvest.setActualCollectAmount(BigDecimal.ZERO);
        busItemInvest.setActualCollectInterest(BigDecimal.ZERO);
        busItemInvest.setActualCollectPrincipal(BigDecimal.ZERO);
        BigDecimal lx= Calculator.getInterest(amount,basItem);// 获取利息
        busItemInvest.setActualUncollectAmount(amount.add(lx));
        busItemInvest.setActualUncollectInterest(lx);
        busItemInvest.setActualUncollectPrincipal(amount);
        busItemInvest.setAddip(IpUtils.get());
        busItemInvest.setAdditionalRateAmount(BigDecimal.ZERO);
        busItemInvest.setAddtime(new Date());
        busItemInvest.setAutoId(null);
        busItemInvest.setCollectAmount(amount.add(lx));
        busItemInvest.setCollectInterest(lx);
        busItemInvest.setCollectPrincipal(amount);
        busItemInvest.setInvestAmount(amount);
        busItemInvest.setInvestCurrent(1);
        busItemInvest.setInvestDealAmount(amount);
        String investOrder="SXT_TZ"+ RandomCodesUtils.createRandom(false,8)+id;
        busItemInvest.setInvestOrder(investOrder);
        busItemInvest.setInvestStatus(0);
        busItemInvest.setInvestType(1);
        busItemInvest.setItemId(itemId);
        busItemInvest.setSpecialMarks(null);
        busItemInvest.setUpdatetime(new Date());
        busItemInvest.setUserId(id);
        AssertUtil.isTrue(busItemInvestDao.insert(busItemInvest)<1, P2pConstant.OP_FAILED_MSG);

        // 积分更新
        BusUserIntegral busUserIntegral=busUserIntegralDao.queryBusUserInteGralByUserId(id);
        busUserIntegral.setUsable(busUserIntegral.getUsable()+100);
        busUserIntegral.setTotal(busUserIntegral.getTotal()+100);
        AssertUtil.isTrue(busUserIntegralDao.update(busUserIntegral)<1,P2pConstant.OP_FAILED_MSG);
        // 更新用户统计信息
        BusUserStat busUserStat= busUserStatDao.queryBusUserStatByUserId(id);
        busUserStat.setInvestCount(busUserStat.getInvestCount()+1);
        busUserStat.setInvestAmount(busUserStat.getInvestAmount().add(amount));
        AssertUtil.isTrue(busUserStatDao.update(busUserStat)<1,P2pConstant.OP_FAILED_MSG);

        // 总金额=可用金额+冻结金额+代收利息金额
        BusAccount busAccount= busAccountDao.queryBusAccountByUserId(id);
        busAccount.setCash(busAccount.getCash().add(amount.negate()));// 设置可提现金额
        busAccount.setFrozen(busAccount.getFrozen().add(amount));// 设置冻结金额
        busAccount.setTotal(busAccount.getTotal().add(lx));// 设置总金额
        busAccount.setUsable(busAccount.getUsable().add(amount.negate()));// 可用金额
        busAccount.setWait(busAccount.getWait().add(lx));
        AssertUtil.isTrue(busAccountDao.update(busAccount)<1,P2pConstant.OP_FAILED_MSG);
        // 添加操作日志记录
        BusAccountLog busAccountLog=new BusAccountLog();
        busAccountLog.setAddtime(new Date());
        busAccountLog.setUserId(id);
        busAccountLog.setAddip(IpUtils.get());
        busAccountLog.setOperMoney(amount);
        busAccountLog.setUsable(busAccount.getUsable());
        busAccountLog.setCash(busAccount.getCash());
        busAccountLog.setFrozen(busAccount.getFrozen());
        busAccountLog.setWait(busAccount.getWait());
        busAccountLog.setOperType("用户投标");
        busAccountLog.setTotal(busAccount.getTotal());
        busAccountLog.setRemark("用户投标成功!");
        busAccountLog.setRepay(BigDecimal.ZERO);
        busAccountLog.setBudgetType(2);
        busAccountLog.setTradeUserId(null);
        AssertUtil.isTrue(busAccountLogDao.insert(busAccountLog)<1,P2pConstant.OP_FAILED_MSG);

        // 更新项目投资进度信息
        basItem.setItemOngoingAccount(basItem.getItemOngoingAccount().add(amount));
        BigDecimal itemAccount=basItem.getItemAccount();
        BigDecimal result= basItem.getItemOngoingAccount().divide(basItem.getItemAccount(),2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        basItem.setItemScale(result);
        BigDecimal syAmount=basItem.getItemAccount().add(basItem.getItemOngoingAccount().negate());
        if(syAmount.compareTo(basItem.getItemSingleMinInvestment())<0){
            basItem.setItemStatus(ItemStatus.INTERCEPT_COMPLETE);
        }
        AssertUtil.isTrue(basItemDao.update(basItem)<1,P2pConstant.OP_FAILED_MSG);


    }

    private void checkParams(BigDecimal amount, String password, Integer itemId) {
        AssertUtil.isTrue(amount.compareTo(BigDecimal.ZERO)<=0,"投资金额非法！");
        AssertUtil.isTrue(StringUtils.isBlank(password),"交易密码不能为空！");
        AssertUtil.isTrue(itemId==null||itemId<1,"订单号有误！");

    }
}
