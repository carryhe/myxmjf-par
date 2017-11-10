package cn.hexg.xm.service.impl;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.db.dao.*;
import cn.hexg.xm.po.*;
import cn.hexg.xm.service.IBasUserService;
import cn.hexg.xm.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class BasUserServiceImpl implements IBasUserService {

    @Resource
    private IBasUserDao basUserDao;
    @Resource
    private BasUserInfoDao basUserInfoDao;
    @Resource
    private BasUserSecurityDao basUserSecurityDao;
    @Resource
    private BusAccountDao busAccountDao;
    @Resource
    private BusUserIntegralDao busUserIntegralDao;
    @Resource
    private BusIncomeStatDao busIncomeStatDao;
    @Resource
    private BusUserStatDao busUserStatDao;
    @Resource
    private BasExperiencedGoldDao basExperiencedGoldDao;
    @Resource
    private SysLogDao sysLogDao;

    @Override
    public BasUser queryBasUserByPhone(String phone) {
        return basUserDao.queryBasUserByPhone(phone);
    }

    @Override
    public void saveUser(String phone,String password) {
        /**
         * 1.参数合法性校验
         * 2.手机号唯一
         * 3.用户信息注册 数据初始化
         * bas_user 表 存入用户基本信息
         *
         */
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(password),"请输入个人密 码!");
        AssertUtil.isTrue(null!=queryBasUserByPhone(phone),"该手机号已注 册,请更换手机号!");
        AssertUtil.isTrue(!PhoneRegularUtil.isMobileNO(phone),"请输入正确的手机号！");
        //进行添加用户信息向bas_user表中添加
        Integer userId = initBasUser(phone, password);
        //进行其他表的初始化
       // 初始化用户基本信息
        initBasUserInfo(userId);
        // 用户安全信息表
        initBasUserSecurity(userId);
        // 用户账户信息初始化
        initBusAccount(userId);
        // 用户基本初始化方法
        initBusUserIntegral(userId);
        // 用户收益初始化
        initBusIncomeStat(userId);
        // 初始化用户统计信息
        initBusUserStat(userId);
        //  用户体验今信息初始化
        initBasExperiencedGold(userId);
        // 初始化系统日志
        initSysLog(userId);

    }

    /**
     * 进行用户登录
     * @param phone
     * @param password
     */
    @Override
    public BasUser userLogin(String phone,String password) {
        //进行校验用户信息
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(password),"密码不能为空！");
        //根据用户的手机进行查询
        BasUser basUser = basUserDao.queryBasUserByPhone(phone);
        AssertUtil.isTrue(basUser==null,"该用户不存在！");
        //获取用户的盐
        String salt = basUser.getSalt();
        AssertUtil.isTrue(StringUtils.isBlank(salt),"系统异常！");
        password = MD5.toMD5(password + salt);
        //获取用户的password
        String basUserPassword = basUser.getPassword();
        AssertUtil.isTrue(StringUtils.isBlank(basUserPassword),"系统异常！");
        AssertUtil.isTrue(!password.equalsIgnoreCase(basUserPassword),"密码不正确！");
        basUser.setLastLoginIp(IpUtils.get());
        basUser.setLastLoginTime(new Date());
        AssertUtil.isTrue(basUserDao.update(basUser)<1,P2pConstant.OP_FAILED_MSG);
        return basUser;
    }

    private void initSysLog(Integer userId) {
        SysLog sysLog=new SysLog();
        sysLog.setOperating("用户注册");
        sysLog.setAddtime(new Date());
        sysLog.setAddip(IpUtils.get());
        sysLog.setCode("register");
        sysLog.setResult(1);
        sysLog.setType(4);
        sysLog.setUserId(userId);
        AssertUtil.isTrue(sysLogDao.insert(sysLog)<1,P2pConstant.OP_FAILED_MSG);
    }

    private void initBasExperiencedGold(Integer userId) {
        BasExperiencedGold basExperiencedGold=new BasExperiencedGold();
        basExperiencedGold.setWay("register");
        basExperiencedGold.setUserId(userId);
        basExperiencedGold.setUsefulLife(10);
        basExperiencedGold.setStatus(2);
        basExperiencedGold.setRate(BigDecimal.valueOf(1));
        basExperiencedGold.setGoldName("注册体验金");
        basExperiencedGold.setExpiredTime(DateUtils.addDays(new Date(),30));
        basExperiencedGold.setAmount(BigDecimal.valueOf(2888));
        basExperiencedGold.setAddtime(new Date());
        basExperiencedGold.setAddip(IpUtils.get());
        AssertUtil.isTrue(basExperiencedGoldDao.insert(basExperiencedGold)<1,P2pConstant.OP_FAILED_MSG);

    }

    private void initBusUserStat(Integer userId) {
        BusUserStat busUserStat=new BusUserStat();
        busUserStat.setUserId(userId);
        busUserStat.setRechargeCount(0);
        busUserStat.setRechargeAmount(BigDecimal.ZERO);
        busUserStat.setInvestLaveAmount(BigDecimal.ZERO);
        busUserStat.setInvestCount(0);
        busUserStat.setInvestAmount(BigDecimal.ZERO);
        busUserStat.setCouponCount(0);
        busUserStat.setCouponAmount(BigDecimal.ZERO);
        busUserStat.setCashCount(0);
        busUserStat.setCashAmount(BigDecimal.ZERO);
        AssertUtil.isTrue(busUserStatDao.insert(busUserStat)<1,P2pConstant.OP_FAILED_MSG);
    }


    private void initBusIncomeStat(Integer userId) {
        BusIncomeStat busIncomeStat=new BusIncomeStat();
        busIncomeStat.setWaitIncome(BigDecimal.ZERO);
        busIncomeStat.setUserId(userId);
        busIncomeStat.setTotalIncome(BigDecimal.ZERO);
        busIncomeStat.setEarnedIncome(BigDecimal.ZERO);
        AssertUtil.isTrue(busIncomeStatDao.insert(busIncomeStat)<1,P2pConstant.OP_FAILED_MSG);
    }

    private void initBusUserIntegral(Integer userId) {
        BusUserIntegral busUserIntegral=new BusUserIntegral();
        busUserIntegral.setTotal(0);
        busUserIntegral.setUsable(0);
        busUserIntegral.setUserId(userId);
        AssertUtil.isTrue(busUserIntegralDao.insert(busUserIntegral)<1,P2pConstant.OP_FAILED_MSG);
    }

    private void initBusAccount(Integer userId) {
        BusAccount busAccount=new BusAccount();
        busAccount.setWait(BigDecimal.ZERO);
        busAccount.setUserId(userId);
        busAccount.setUsable(BigDecimal.ZERO);
        busAccount.setTotal(BigDecimal.ZERO);
        busAccount.setRepay(BigDecimal.ZERO);
        busAccount.setFrozen(BigDecimal.ZERO);
        busAccount.setCash(BigDecimal.ZERO);
        AssertUtil.isTrue(busAccountDao.insert(busAccount)<1,P2pConstant.OP_FAILED_MSG);
    }

    private void initBasUserSecurity(Integer userId) {
        BasUserSecurity basUserSecurity=new BasUserSecurity();
        basUserSecurity.setPhoneStatus(1);
        basUserSecurity.setUserId(userId);
        AssertUtil.isTrue(basUserSecurityDao.insert(basUserSecurity)<1,P2pConstant.OP_FAILED_MSG);;
    }

    private void initBasUserInfo(Integer userId) {
        BasUserInfo basUserInfo=new BasUserInfo();
        basUserInfo.setUserId(userId);
        basUserInfo.setCustomerType(0);
        String code= RandomCodesUtils.createRandom(false,5);
        basUserInfo.setInviteCode(code);
        basUserInfo.setCashLimit(0);
        AssertUtil.isTrue(basUserInfoDao.insert(basUserInfo)<1,P2pConstant.OP_FAILED_MSG);
    }

    /**
     * 进行注册时候初始化，用户表
     * @param phone
     * @param password
     * @return
     */
    private Integer initBasUser(String phone, String password) {
        BasUser basUser = new BasUser();
        basUser.setType(1);
        basUser.setStatus(1);
        //进行设置用户的密码，进行加盐处理
        String salt = RandomCodesUtils.createRandom(false, 4);
        password = MD5.GetMD5Code(password + salt);
        basUser.setPassword(password);
        basUser.setSalt(salt);
        basUser.setReferer("PC");
        basUser.setPassword(password);
        basUser.setMobile(phone);
        basUser.setAddtime(new Date()) ;
        basUser.setAddip(IpUtils.get());
        //进行插入
        AssertUtil.isTrue(basUserDao.insert(basUser)<1, P2pConstant.OP_FAILED_MSG);
        basUser.setUsername("SXT_P2P"+basUser.getId());
        AssertUtil.isTrue(basUserDao.update(basUser)<1,P2pConstant.OP_FAILED_MSG);
        return basUser.getId();
    }
}
