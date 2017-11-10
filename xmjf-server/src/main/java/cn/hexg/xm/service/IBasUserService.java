package cn.hexg.xm.service;
import cn.hexg.xm.po.BasUser;

public interface IBasUserService {
    public BasUser queryBasUserByPhone(String phone);

   public void saveUser(String phone, String password);

   public BasUser userLogin(String phone,String password);
}
