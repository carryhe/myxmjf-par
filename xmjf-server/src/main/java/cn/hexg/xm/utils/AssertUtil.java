package cn.hexg.xm.utils;


import cn.hexg.xm.exceptions.ParamsExcetion;

public class AssertUtil {
	
	public static void isTrue(Boolean flag,String errorMsg) {
		if(flag){
			throw new ParamsExcetion(errorMsg);
		}
	}
	
	
	public static void isTrue(Boolean flag,String errorMsg,Integer errorCode) {
		if(flag){
			throw new ParamsExcetion(errorMsg,errorCode);
		}
	}
	

	
}
