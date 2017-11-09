package cn.hexg.xm.constant;

public class P2pConstant {

    public final static String OP_SUCCESS_MSG = "操作成功";
    public final static Integer OP_SUCCESS_CODE = 200;
    public final static String OP_FAILED_MSG = "操作失败";
    public final static Integer OP_FAILED_CODE = 300;
    //图片验证码
    public final static String PICTURE_VERIFY_KEY = "image_code";


    //  短信发送常量
    public final static String TAOBAO_SEND_PHONE_URL = "http://gw.api.taobao.com/router/rest";
    public final static String TAOBAO_APP_KEY = "24664902";
    public final static String TAOBAO_APP_SECRET = "04e5d0670a772219984bf206cb85c55b";
    public final static String TAOBAO_SMS_TYPE = "normal";
    public final static String TAOBAO_SMS_FREE_SIGN_NAME = "小马金服";
    public final static String TAOBAO_SMS_TEMPLATE_CODE = "SMS_109450111";

    // 手机验证码session key 值
    public final static String PHONE_VERIFY_CODE = "XM_00001_";
    public final static String PHONE_VERIFY_CODE_EXPIR_TIME = "XM_00002_";
    //手机验证码存活的时间180秒
    public final static long PHONE_VERIFY_CODE_TIME = 180;


}
