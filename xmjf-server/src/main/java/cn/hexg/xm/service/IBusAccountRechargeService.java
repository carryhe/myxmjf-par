package cn.hexg.xm.service;



import cn.hexg.xm.dto.CallBackDto;
import cn.hexg.xm.dto.PayDto;
import cn.hexg.xm.po.BusAccountRecharge;
import cn.hexg.xm.query.BusAccountRechargeQuery;
import cn.hexg.xm.utils.PageList;

import java.util.List;


public interface IBusAccountRechargeService {

    // 添加充值记录
    public PayDto addBusAccountRechargeAndBuildPayInfo(BusAccountRecharge busAccountRecharge, String password);


    public void  updateBusAccountRecharge(CallBackDto callBackDto, Integer userId);

    public PageList queryAccountRechargeListByParams(BusAccountRechargeQuery busAccountRechargeQuery);



}
