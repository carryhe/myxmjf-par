package cn.hexg.xm.enums;

/**
 * Created by lp on 2017/11/13.
 */
public enum PayStatus {
    UN_PAY(0),// 未支付
    PAIED(1); // 已支付
    PayStatus(Integer state) {
        this.state = state;
    }

    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
