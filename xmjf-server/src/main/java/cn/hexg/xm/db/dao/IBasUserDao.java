package cn.hexg.xm.db.dao;

import cn.hexg.xm.base.BaseDao;
import cn.hexg.xm.po.BasUser;
import org.apache.ibatis.annotations.Param;

public interface IBasUserDao extends BaseDao<BasUser> {

    public  BasUser queryBasUserByPhone(@Param("phone")String phone);

}