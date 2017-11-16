package cn.hexg.xm.service;

import java.math.BigDecimal;

public interface IBusItemInvestService {
    //进行添加投资记录信息
    void addBusItemInvest(Integer id, BigDecimal amount, Integer itemId, String password);
}
