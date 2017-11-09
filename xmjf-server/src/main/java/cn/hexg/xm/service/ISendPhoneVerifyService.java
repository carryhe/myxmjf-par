package cn.hexg.xm.service;

public interface ISendPhoneVerifyService {

    /**
     * 发送手机短信验证码
     * @param phone
     * @param code
     */
    public  void sendPhoneCode(String phone,String code) throws  Exception;
}
