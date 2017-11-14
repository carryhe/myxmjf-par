package cn.hexg.xm.db.dao;


import cn.hexg.xm.base.BaseDao;
import cn.hexg.xm.po.BusAccountRecharge;
import cn.hexg.xm.query.BusAccountRechargeQuery;

import java.util.List;

public interface BusAccountRechargeDao extends BaseDao<BusAccountRecharge> {

    public  BusAccountRecharge queryBusAccountRechargeByOrderNo(String orderNo);

    public List<BusAccountRecharge> queryAccountRechargeListByParams(BusAccountRechargeQuery busAccountRechargeQuery);

}