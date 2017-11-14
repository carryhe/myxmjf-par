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
    // 交易验证码
    public  final  static  String PICTURE_TRADE_VERIFY_KEY="XM_000003";

    /**
     * 第三方支付参数配置
     */
    /**
     * 合作身份者PID，签约账号，由16位纯数字组成的字符串，请登录商户
     */
    public final static String  PARTNER = "9796636105929474";

    /**
     *  MD5密钥，安全检验码，由数字和字母组成的32位字符串，请登录商户后
     */
    public final static String KEY = "23fd93167d2141708cdd67ef0ca5531d";

    /**
     * 商户号（8位数字)
     */
    public final static String  USER_SELLER = "74754404";

    /**
     *  服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可
     */
    public final static String  NOTIFY_URL = "http://www.shsxt.com:8080/account/callback";

    /**
     * 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
    public final static String  RETURN_URL = "http://www.shsxt.com:8080/account/callBack";

    /**
     * 支付地址，必须外网可以正常访问
     */
    public final static String  GATEWAY_NEW = "http://payment.passpay.net/PayOrder/payorder";

    public  final  static  String BODY="PC端用户充值操作";
    public  final  static  String SUBJECT="PC端用户充值";
    public  final  static  String ORDER_FAILED_MSG="订单异常，请联系管理员!";
    public  final  static  String ORDER_SUCCESS="TRADE_SUCCESS";

}
