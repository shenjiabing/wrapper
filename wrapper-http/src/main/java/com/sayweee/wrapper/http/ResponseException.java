package com.sayweee.wrapper.http;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class ResponseException extends RuntimeException {

    public final static int ERROR_UNKNOWN = -100; //未知异常
    public final static int ERROR_PARSE = -101; //解析出错
    public final static int ERROR_RESPONSE_STATUS_FAILED = -102; //response 返回 false
    //如无需校验data，请使用BaseBean或者N
    public final static int ERROR_RESPONSE_DATA_NULL = -103; //response 返回 data = null
    //如无需校验data，请使用BaseBean或者N
    public final static int ERROR_RESPONSE_DATA_CONTENT_NULL = -104; //response 返回 data value = null

    public int errorCode;
    public String rawData;

    public ResponseException(String message) {
        super(message);
    }

    public ResponseException(Throwable cause) {
        super(cause);
    }

    public ResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseException(int errorCode, String message, String rawData) {
        super(message);
        this.errorCode = errorCode;
        this.rawData = rawData;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
