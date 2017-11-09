package cn.hexg.xm.service;


import cn.hexg.xm.po.BasUser;

/**
 * Created by lp on 2017/11/7.
 */
public interface IBasUserService {
    public BasUser queryBasUserByPhone(String phone);

    void saveUser(String phone, String password);
}
