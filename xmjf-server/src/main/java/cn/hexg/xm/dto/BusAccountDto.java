package cn.hexg.xm.dto;

import java.math.BigDecimal;

/**
 * Created by lp on 2017/11/16.
 */
public class BusAccountDto {
    private BigDecimal total;// 总金额
    private BigDecimal waitCapital;//代收金额
    private BigDecimal waitIncome;// 代收利息
    private BigDecimal cash;//可提现金额
    private BigDecimal frozen;//冻结金额
    private BigDecimal lock;//锁定余额

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getWaitCapital() {
        return waitCapital;
    }

    public void setWaitCapital(BigDecimal waitCapital) {
        this.waitCapital = waitCapital;
    }

    public BigDecimal getWaitIncome() {
        return waitIncome;
    }

    public void setWaitIncome(BigDecimal waitIncome) {
        this.waitIncome = waitIncome;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }

    public BigDecimal getLock() {
        return lock;
    }

    public void setLock(BigDecimal lock) {
        this.lock = lock;
    }
}
