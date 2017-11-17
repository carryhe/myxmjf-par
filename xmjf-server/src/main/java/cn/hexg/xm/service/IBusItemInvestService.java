package cn.hexg.xm.service;

import java.math.BigDecimal;
import java.util.Map;

public interface IBusItemInvestService {
    //进行添加投资记录信息
    void addBusItemInvest(Integer id, BigDecimal amount, Integer itemId, String password);

    Map<String,Object> queryItemInvestsFiveMonthByUserId(Integer id);
}
