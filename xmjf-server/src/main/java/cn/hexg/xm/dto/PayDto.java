package cn.hexg.xm.dto;

/**
 * Created by lp on 2017/11/13.
     * partner	商户PID	必填
     user_seller	商户号	必填
     out_order_no	商户网站订单号（唯一）	必填
     subject	订单名称	必填
     total_fee	订单价格	必填
     body	订单描述	选填
     notify_url	异步回调地址	必填
     return_url	同步回调地址	必填
     sign	校验码	必填

 */
public class PayDto {
    // 商品PID
    private String partner;
    // 商户号
    private String userSeller;
    // 商户网站订单号
    private String outOrderNo;
    // 订单名称
    private String subject;
    // 订单价格
    private String totalFee;
    // 订单描述
    private String body;
    // 异步回调地址
    private String notifyUrl;
    //同步回调地址
    private String returnUrl;
    // 签名
    private String sign;
    // 支付地址
    private String GatewayNew;

    public String getGatewayNew() {
        return GatewayNew;
    }

    public void setGatewayNew(String gatewayNew) {
        GatewayNew = gatewayNew;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getUserSeller() {
        return userSeller;
    }

    public void setUserSeller(String userSeller) {
        this.userSeller = userSeller;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
