package cn.hexg.xm.constant;

/**
 * Created by lp on 2017/11/15.
 */
public class ItemStatus {
    public static  final int NEW=0; //开始
    public static  final int WAITOPEN=1; //等待开放
    public static  final int FIRST=2;//初审中
    public static final int OPEN=10;  //开放
    public static final int FAILURE=11; //初审失败
    public static final int CANCEL=13; //测回审核
    public static final int CANCEL_SUCCESS=14; //测回成功
    public static final int INTERCEPT_COMPLETE=18;  //截标
    public static final int FULL_COMPLETE=20;  //满标
    public static final int FULL_SUCCESS=21;   //满标成功
    public static final int FULL_FAILURE=22;   //复审失败
    public static final int FULL_AUDIT=23;   //复审审核
    public static final int THIRD_FULL_AUDIT=24;   //第三方放款失败
    public static final int REPAYING=30; //复审通过待还款
    public static final int REPAY_PART=31; //部分还款
    public static final int REPAY_COMPLETE=32; //还款成功
}
