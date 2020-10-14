package com.sayweee.wrapper.http;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class ExceptionHandler {

    public final static int ERROR_UNAUTHORIZED = 401;
    public final static int ERROR_FORBIDDEN = 403;
    public final static int ERROR_NOT_FOUND = 404;
    public final static int ERROR_REQUEST_TIMEOUT = 408;
    public final static int ERROR_INTERNAL_SERVER_ERROR = 500;
    public final static int ERROR_SERVICE_UNAVAILABLE = 503;

    //-100系列错误定义的http层，复用原有定义
    public final static int ERROR_UNKNOWN = ResponseException.ERROR_UNKNOWN; //未知异常
    public final static int ERROR_PARSE = ResponseException.ERROR_PARSE; //解析出错
    public final static int ERROR_RESPONSE_STATUS_FAILED = ResponseException.ERROR_RESPONSE_STATUS_FAILED; //response 返回 false
    //如无需校验data，请使用BaseBean或者N
    public final static int ERROR_RESPONSE_DATA_NULL = ResponseException.ERROR_RESPONSE_DATA_NULL; //response 返回 data = null
    //如无需校验data，请使用BaseBean或者N
    public final static int ERROR_RESPONSE_DATA_CONTENT_NULL = ResponseException.ERROR_RESPONSE_DATA_CONTENT_NULL; //response 返回 data content = null

    public final static int ERROR_NETWORK = -201; //网络问题
    public final static int ERROR_CONNECT = -202; //连接问题
    public final static int ERROR_CONNECT_SSL = -203; //SSL问题


    public static ResponseException handleException(Throwable e) {
        String error;
        int code;
        if (e instanceof HttpException) {
            code = ((HttpException) e).code();
            switch (code) {
                case ERROR_UNAUTHORIZED:
                    error = "unauthorized";
                    break;
                case ERROR_FORBIDDEN:
                    error = "request forbidden";
                    break;
                case ERROR_NOT_FOUND:
                    error = "request not found";
                    break;
                case ERROR_REQUEST_TIMEOUT:
                    error = "request timeout";
                    break;
                case ERROR_INTERNAL_SERVER_ERROR:
                    error = "internal server error";
                    break;
                case ERROR_SERVICE_UNAVAILABLE:
                    error = "service unavailable";
                    break;
                default:
                    error = "unknown error";
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            error = "parse error";
            code = ERROR_PARSE;
        } else if (e instanceof ConnectException) {
            error = "connect error";
            code = ERROR_CONNECT;
        } else if (e instanceof javax.net.ssl.SSLException) {
            error = "ssl error";
            code = ERROR_CONNECT_SSL;
        } else if (e instanceof ConnectTimeoutException) {
            error = "connect timeout";
            code = ERROR_CONNECT;
        } else if (e instanceof java.net.SocketTimeoutException) {
            error = "socket timeout";
            code = ERROR_CONNECT;
        } else if (e instanceof java.net.UnknownHostException) {
            error = "unknown host";
            code = ERROR_CONNECT;
        } else {
            error = "unknown error";
            code = ERROR_UNKNOWN;
        }
        ResponseException exception = new ResponseException(error, e);
        exception.errorCode = code;
        return exception;
    }


    public static ResponseException handleException(int code, String message) {
        ResponseException exception = new ResponseException(message);
        exception.errorCode = code;
        return exception;
    }

    public static boolean isServerError(int errorCode) {
        return errorCode >= 400 && errorCode < 600;
    }

    public static boolean isResponseError(int errorCode) {
        return errorCode == ERROR_RESPONSE_STATUS_FAILED || errorCode == ERROR_RESPONSE_DATA_NULL || errorCode == ERROR_RESPONSE_DATA_CONTENT_NULL;
    }

    public static boolean isParseError(int errorCode) {
        return errorCode == ERROR_PARSE;
    }

    public static boolean isConnectError(int errorCode) {
        return errorCode == ERROR_CONNECT_SSL || errorCode == ERROR_CONNECT || errorCode == ERROR_NETWORK;
    }

    public static boolean isResponseStatusError(ResponseException e) {
        return e != null && e.errorCode == ERROR_RESPONSE_STATUS_FAILED;
    }

    public static boolean isResponseDataError(ResponseException e) {
        return e != null && e.errorCode == ERROR_RESPONSE_DATA_NULL;
    }

}
