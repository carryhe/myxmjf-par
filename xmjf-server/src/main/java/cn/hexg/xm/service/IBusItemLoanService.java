package cn.hexg.xm.service;

import cn.hexg.xm.base.BaseService;
import cn.hexg.xm.po.BusItemLoan;

public interface IBusItemLoanService  {

    public BusItemLoan queryBusItemLoanByItemId(Integer itemId);
}
