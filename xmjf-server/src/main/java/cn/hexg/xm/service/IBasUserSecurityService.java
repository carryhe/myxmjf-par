package cn.hexg.xm.service;

import cn.hexg.xm.po.BasUserSecurity;

/**
 * 用户信息安全的服务
 */
public interface IBasUserSecurityService {

    public BasUserSecurity queryUserSecurityByUserId(int userId);

    void queryUserAuthStatus(Integer id);

    void updateUserSercuityInfo(Integer id, String realName, String idCard, String payPassword);
}
