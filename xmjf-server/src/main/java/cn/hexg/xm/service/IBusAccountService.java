package cn.hexg.xm.service;

import cn.hexg.xm.dto.BusAccountDto;
import cn.hexg.xm.po.BusAccount;

public interface IBusAccountService {

    public BusAccount queryBusAccountByUserId(Integer userId);

    BusAccountDto queryBusAccountInfoByUserId(Integer id);
}
