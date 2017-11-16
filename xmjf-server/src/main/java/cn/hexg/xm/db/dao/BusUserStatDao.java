package cn.hexg.xm.db.dao;

import cn.hexg.xm.base.BaseDao;
import cn.hexg.xm.po.BusUserStat;
import cn.hexg.xm.po.BusUserStatKey;

public interface BusUserStatDao extends BaseDao<BusUserStat> {


    BusUserStat queryBusUserStatByUserId(Integer id);
}