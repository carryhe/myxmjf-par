package cn.hexg.xm.exceptions;

public class LoginException extends RuntimeException {

    private String errorMsg="操作失败";
    private Integer errorCode=300;

    public LoginException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public LoginException(String errorMsg, Integer errorCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
