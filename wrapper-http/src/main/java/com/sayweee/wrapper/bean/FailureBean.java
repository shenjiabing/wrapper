package com.sayweee.wrapper.bean;


import com.sayweee.wrapper.http.ExceptionHandler;
import com.sayweee.wrapper.http.ResponseException;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:
 */
public class FailureBean {

    private boolean result;  //请求结果
    private String message; //请求返回提示或者异常提示  请求提示优先
    private ResponseException exception;

    public static FailureBean handle(boolean result, String message, ResponseException exception) {
        return new FailureBean(result, message, exception);
    }

    public FailureBean(boolean result, String message, ResponseException exception) {
        this.result = result;
        this.message = message;
        this.exception = exception;
    }

    public boolean isResponseStatusError() {
        return ExceptionHandler.isResponseStatusError(exception);
    }

    public boolean isResponseDataError() {
        return ExceptionHandler.isResponseDataError(exception);
    }

    public int getErrorCode() {
        return exception != null ? exception.errorCode : ExceptionHandler.ERROR_UNKNOWN;
    }

    public String getMessage() {
        return message != null ? message : exception != null ? exception.getMessage() : "";
    }

    /**
     * 获取请求ex，可能为空
     *
     * @return
     */
    public ResponseException getException() {
        return exception;
    }

}
