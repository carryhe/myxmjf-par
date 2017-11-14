package cn.hexg.xm.db.dao;

import cn.hexg.xm.base.BaseDao;
import cn.hexg.xm.po.BasUserSecurity;

public interface BasUserSecurityDao extends BaseDao<BasUserSecurity> {

    public BasUserSecurity queryUserSecurityByUserId(int userId);
    
}