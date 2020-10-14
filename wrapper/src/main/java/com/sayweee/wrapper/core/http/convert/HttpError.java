package com.sayweee.wrapper.core.http.convert;

import androidx.annotation.Nullable;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/26.
 * Desc:    自定义异常
 */
public class HttpError extends RuntimeException {

    public final static int CODE_EXCEPTION = -1;       //异常
    public final static int CODE_RESPONSE_RESULT_FAILED = 1;    //返回result false
    public final static int CODE_RESPONSE_DATA_ERROR = 2;       //不符合约定的数据格式
    public final static int CODE_PARSE_ERROR = 3;         //解析异常
    public final static int CODE_NETWORK_ERROR = 4;       //网络问题
    public final static int CODE_SERVER_ERROR = 4;        //服务器问题
    public final static int CODE_OTHER_ERROR = 5;         //其他异常

    public String msg;
    public int code;
    @Nullable
    public final transient Object body;

    public HttpError(String msg) {
        this(msg, null);
    }

    public HttpError(String msg, @Nullable Object body) {
        super(msg);
        if (body instanceof Throwable) {
            initCause((Throwable) body);
        }
        this.msg = msg != null ? msg : "null";
        this.body = body;
    }

    public HttpError(int code, String msg, @Nullable Object body) {
        super(msg);
        if (body instanceof Throwable) {
            initCause((Throwable) body);
        }
        this.code = code;
        this.msg = msg != null ? msg : "null";
        this.body = body;
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public Object getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpError{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", body=" + body +
                '}';
    }
}
