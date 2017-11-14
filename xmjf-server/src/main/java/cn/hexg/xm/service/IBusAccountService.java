package cn.hexg.xm.service;

import cn.hexg.xm.po.BusAccount;

public interface IBusAccountService {

    public BusAccount queryBusAccountByUserId(Integer userId);
}
