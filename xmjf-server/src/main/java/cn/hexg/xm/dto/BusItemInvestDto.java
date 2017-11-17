package cn.hexg.xm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.imageio.plugins.common.BogusColorSpace;

import java.math.BigDecimal;
import java.util.Date;

public class BusItemInvestDto {
    private BigDecimal investAmount;
    private String mobile;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date addtime;


    private String month;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }
}
