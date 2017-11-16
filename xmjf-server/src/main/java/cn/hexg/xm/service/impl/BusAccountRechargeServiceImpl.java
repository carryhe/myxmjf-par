package cn.hexg.xm.service.impl;

import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.db.dao.*;
import cn.hexg.xm.dto.CallBackDto;
import cn.hexg.xm.dto.PayDto;
import cn.hexg.xm.enums.PayStatus;
import cn.hexg.xm.enums.RechargeType;
import cn.hexg.xm.po.*;
import cn.hexg.xm.query.BusAccountRechargeQuery;
import cn.hexg.xm.service.IBusAccountRechargeService;
import cn.hexg.xm.utils.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class BusAccountRechargeServiceImpl implements IBusAccountRechargeService {
    @Resource
    private BusAccountRechargeDao busAccountRechargeDao;
    @Resource
    private IBasUserDao basUserDao;

    @Resource
    private BasUserSecurityDao basUserSecurityDao;


    @Resource
    private BusAccountLogDao busAccountLogDao;

    @Resource
    private BusAccountDao busAccountDao;

    @Override
    public PayDto addBusAccountRechargeAndBuildPayInfo(BusAccountRecharge busAccountRecharge, String password) {
       BasUser basUser= basUserDao.queryById(busAccountRecharge.getUserId());
       AssertUtil.isTrue(null==basUser,"未登录");
       BasUserSecurity basUserSecurity= basUserSecurityDao.queryUserSecurityByUserId(basUser.getId());
       AssertUtil.isTrue(basUserSecurity.getRealnameStatus().equals(0),"用户未认证!");
       /*
       * 注意交易密码的加密操作
       * */
       AssertUtil.isTrue(!basUserSecurity.getPaymentPassword().equals(password),"交易密码不正确!");
       String orderNo= MyStringUtils.getOrderNo();
       busAccountRecharge.setOrderNo(orderNo);
       busAccountRecharge.setType(PayStatus.UN_PAY.getState());
       busAccountRecharge.setFeeAmount(BigDecimal.ZERO);
       busAccountRecharge.setFeeRate(BigDecimal.ZERO);
       busAccountRecharge.setResource("PC端充值");
       busAccountRecharge.setType(RechargeType.WEB.getType());
       busAccountRecharge.setRemark("PC端充值"+busAccountRecharge.getRechargeAmount()+"元");
       busAccountRecharge.setAddtime(new Date());
       AssertUtil.isTrue( busAccountRechargeDao.insert(busAccountRecharge)<1, P2pConstant.OP_FAILED_MSG);
       // 构建充值信息
        PayDto payDto=buildPayInfo(orderNo,busAccountRecharge.getRechargeAmount());
        return payDto;
    }



    /**
     * 构建支付请求vo 对象
     * @param orderNo
     * @param amount
     * @return
     */
    public PayDto buildPayInfo(String orderNo,BigDecimal amount) {
        PayDto payDto=new PayDto();
        payDto.setBody(P2pConstant.BODY);
        payDto.setNotifyUrl(P2pConstant.NOTIFY_URL);
        payDto.setOutOrderNo(orderNo);
        payDto.setPartner(P2pConstant.PARTNER);
        payDto.setReturnUrl(P2pConstant.RETURN_URL);
        payDto.setSubject(P2pConstant.SUBJECT);
        payDto.setTotalFee(amount.toString());
        payDto.setUserSeller(P2pConstant.USER_SELLER);
        payDto.setGatewayNew(P2pConstant.GATEWAY_NEW);
        payDto.setSign(buildSign(orderNo,amount.toString()));
        return payDto;
    }


    /**
     * 构建加密串
     * @param orderNo  订单号
     * @param total    总金额字符串
     * @return
     */
    private String buildSign(String orderNo, String total) {
        StringBuffer arg = new StringBuffer();
        arg.append("body="+P2pConstant.BODY+"&");
        arg.append("notify_url="+P2pConstant.NOTIFY_URL+"&");
        arg.append("out_order_no="+orderNo+"&");
        arg.append("partner="+P2pConstant.PARTNER+"&");
        arg.append("return_url="+P2pConstant.RETURN_URL+"&");
        arg.append("subject="+P2pConstant.SUBJECT+"&");
        arg.append("total_fee="+total+"&");
        arg.append("user_seller="+P2pConstant.USER_SELLER);
        // 如果存在转义字符，那么去掉转义
        return StringEscapeUtils.unescapeJava(arg.toString());
    }


    @Override
    public void updateBusAccountRecharge(CallBackDto callBackDto, Integer userId) {
        /**
         * 1.基本参数校验
         *    用户是否登录校验
         *    金额校验
         *    订单号
         *    签名
         *    结果
         * 2.签名合法性校验
         * 3.订单是否支付成功校验
         * 4.订单存在性校验
         * 5.订单状态校验  未支付订单方可进行状态更新
         * 6.订单金额是否相等校验
         * 7.订单状态更新
         * 8.充值日志记录添加
         */
        // 参数基本校验
        BasUser basUser=basUserDao.queryById(userId);
        System.out.println(basUser);
        AssertUtil.isTrue(null==userId||null==basUser,"用户未登录!");
        System.out.println(callBackDto.getOutOrderNo());
        System.out.println(callBackDto.getSign());
        System.out.println(callBackDto.getTotalFee());
        System.out.println(callBackDto.getTradeStatus());
        AssertUtil.isTrue(
                org.apache.commons.lang3.StringUtils.isBlank(callBackDto.getTotalFee())||
                        org.apache.commons.lang3.StringUtils.isBlank(callBackDto.getOutOrderNo())||
                        org.apache.commons.lang3.StringUtils.isBlank(callBackDto.getSign())||
                        org.apache.commons.lang3.StringUtils.isBlank(callBackDto.getTradeStatus()),
                P2pConstant.ORDER_FAILED_MSG);
        /**
         * 将out_order_no、total_fee、trade_status、商户PID、商户KEY 的值连接起来，进行md5加密，
         * 而后与sign进行对比，如果相同则通知验证结果是正确，如果不相同则可能数据被篡改，不要进行业务处理，
         * 验证通过之后再判断trade_status是否等于TRADE_SUCCESS，相等则支付成功，如果不等则支付失败。
         */
        // 签名加密校验
        String sign=callBackDto.getOutOrderNo()+callBackDto.getTotalFee()+callBackDto.getTradeStatus()+
                P2pConstant.PARTNER+P2pConstant.KEY;

        Md5Util md5Util=new Md5Util();
        sign=md5Util.encode(sign,null);
        AssertUtil.isTrue(!sign.equals(callBackDto.getSign()),"签名被修改，请联系客服!");
        // 订单支付成功校验
        AssertUtil.isTrue(!callBackDto.getTradeStatus().equals(P2pConstant.ORDER_SUCCESS),P2pConstant.ORDER_FAILED_MSG);
        // 充值订单是否存在校验
        BusAccountRecharge busAccountRecharge=busAccountRechargeDao.queryBusAccountRechargeByOrderNo(callBackDto.getOutOrderNo());
        AssertUtil.isTrue(null==busAccountRecharge,"当前订单不存在，请联系客服!");
        AssertUtil.isTrue(!busAccountRecharge.getStatus().equals(0),"订单状态异常，请联系客服!");
        // 订单金额是否相等判断
        int flag=busAccountRecharge.getRechargeAmount().compareTo(BigDecimal.valueOf(Double.parseDouble(callBackDto.getTotalFee())));
        AssertUtil.isTrue(flag!=0,"订单金额异常，请联系客服!");
        // 更新充值记录
        busAccountRecharge.setStatus(1);// 已支付
        busAccountRecharge.setActualAmount(busAccountRecharge.getRechargeAmount());//实付金额
        AssertUtil.isTrue(busAccountRechargeDao.update(busAccountRecharge)<1,P2pConstant.OP_FAILED_MSG);

       //日志记录添加
        BusAccountLog busAccountLog=new BusAccountLog();
        busAccountLog.setUserId(userId);
        busAccountLog.setOperType("user_recharge_success");
        BigDecimal amount=BigDecimal.valueOf(Double.parseDouble(callBackDto.getTotalFee()));// 充值金额
        busAccountLog.setOperMoney(amount);
        busAccountLog.setBudgetType(1);// 收入日志
        busAccountLog.setTotal(amount);
        busAccountLog.setUsable(amount);
        busAccountLog.setFrozen(BigDecimal.ZERO);
        busAccountLog.setWait(BigDecimal.ZERO);
        busAccountLog.setRepay(BigDecimal.ZERO);
        busAccountLog.setCash(amount);
        busAccountLog.setRemark("用户充值成功!");
        busAccountLog.setAddtime(new Date());
        busAccountLog.setAddip(IpUtils.get());
        BusAccount busAccount=busAccountDao.queryBusAccountByUserId(userId);
        // 账户资金更新
        AssertUtil.isTrue(busAccountDao.uppdateBusAccount(busAccountLog.getUsable(),
                busAccountLog.getFrozen(),
                busAccountLog.getWait(),
                busAccountLog.getCash(),
                busAccountLog.getRepay(),
                userId)<1,P2pConstant.OP_FAILED_MSG);

        // 重新修改日志金额数据
        busAccountLog.setUsable(busAccountLog.getUsable().add(busAccount.getUsable()));
        busAccountLog.setFrozen(busAccountLog.getFrozen().add(busAccount.getFrozen()));
        busAccountLog.setWait(busAccountLog.getWait().add(busAccount.getWait()));
        busAccountLog.setCash(busAccountLog.getCash().add(busAccount.getCash()));
        busAccountLog.setRepay(busAccountLog.getRepay().add(busAccount.getRepay()));
        busAccountLog.setTotal(busAccountLog.getUsable()
                        .add(busAccountLog.getFrozen())
                        .add(busAccountLog.getWait()));
        AssertUtil.isTrue(busAccountLogDao.insert(busAccountLog)<1,P2pConstant.OP_FAILED_MSG);
    }

    @Override
    public PageList queryAccountRechargeListByParams(BusAccountRechargeQuery busAccountRechargeQuery) {
        PageHelper.startPage(busAccountRechargeQuery.getPageNum(),busAccountRechargeQuery.getPageSize());
        List<BusAccountRecharge> busAccountRecharges= busAccountRechargeDao.queryAccountRechargeListByParams(busAccountRechargeQuery);
        return new PageList(busAccountRecharges);
    }


    public static void main(String[] args) {
        System.out.println(Double.parseDouble("0.12"));
    }
}
