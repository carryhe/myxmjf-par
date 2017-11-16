package cn.hexg.xm.db.dao;

import cn.hexg.xm.base.BaseDao;
import cn.hexg.xm.po.BusUserIntegral;

public interface BusUserIntegralDao extends BaseDao<BusUserIntegral> {

    BusUserIntegral queryBusUserInteGralByUserId(Integer id);
}