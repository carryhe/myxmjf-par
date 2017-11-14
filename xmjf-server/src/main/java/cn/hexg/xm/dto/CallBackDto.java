package cn.hexg.xm.dto;

/**
 * Created by lp on 2017/11/13.
 */
public class CallBackDto {
    // 交易金额
    private String totalFee;
    // 交易订单号
    private String outOrderNo;
    //服务端校验码
    private String sign;
    //交易结果流水
    private String tradeStatus;


    public String getTotalFee() {

        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }
}
