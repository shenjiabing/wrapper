package com.sayweee.wrapper.core.http;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.core.http.convert.CommonResponseConverter;
import com.sayweee.wrapper.core.http.convert.HttpError;
import com.sayweee.wrapper.core.model.BaseVo;
import com.sayweee.wrapper.core.model.FailureCode;
import com.sayweee.wrapper.core.model.FailureVo;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public class ResponseCallback<T> implements retrofit2.Callback<T> {
    protected Callback callback;

    public ResponseCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            dispatchResponse(call, response);
        } else {
            //请求code != 200-300
            dispatchFailure(response.code(), new FailureVo(HttpError.CODE_OTHER_ERROR, false, "", ""), call, null);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Logger.error(t);
        String msg = "request failed";
        int code = HttpError.CODE_EXCEPTION;
        int failureCode = FailureCode.FAILURE_RETROFIT;
        String raw = null;
        if (t instanceof HttpError) {
            HttpError error = (HttpError) t;
            code = error.getCode();
            msg = error.getMessage();
            if (error.getBody() instanceof String) {
                raw = (String) error.getBody();
            }
            if (code == HttpError.CODE_RESPONSE_RESULT_FAILED) {
                dispatchFailureResponse(new FailureVo(code, false, msg, raw), call, t);
            }
        } else if (t instanceof UnknownHostException) {
            msg = "network error";
        } else if (t instanceof ConnectException) {
            msg = "network error";
        } else if (t instanceof SocketException) {
            msg = "socket error"; //服务异常
        } else if (t instanceof SocketTimeoutException) {
            msg = "response timeout";
        }
        dispatchFailure(failureCode, new FailureVo(code, false, msg, raw), call, t);
    }


    void dispatchResponse(Call<T> call, Response<T> response) {
        T body = response.body();
        if (body instanceof BaseVo) {
            dispatchSuccess(call, response);
            return;
        }
        try {
            if (body != null) {
                Field field = body.getClass().getDeclaredField(CommonResponseConverter.resultName);
                field.setAccessible(true);
                boolean result = field.getBoolean(body);
                if (result) {
                    dispatchSuccess(call, response);
                } else {
                    String message = null;
                    try {
                        Field messageField = body.getClass().getDeclaredField(CommonResponseConverter.messageName);
                        field.setAccessible(true);
                        message = String.valueOf(messageField.get(body));
                    } catch (Exception e) {

                    }
                    FailureVo data = new FailureVo(HttpError.CODE_RESPONSE_RESULT_FAILED, false, message, null);
                    dispatchFailureResponse(data, call, null); //请求result = false
                }
                return;
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        FailureVo data = new FailureVo(HttpError.CODE_PARSE_ERROR, false, "data parse error", null);
        dispatchFailure(FailureCode.FAILURE_CONVERT, data, call, null);
    }

    /**
     * 分发成功的结果
     *
     * @param call
     * @param response
     */
    public void dispatchSuccess(Call<T> call, Response<T> response) {
        callback.dispatchSuccess(call, response);
    }

    public void dispatchFailureResponse(FailureVo data, Call<T> call, Throwable t) {
        dispatchFailure(FailureCode.FAILURE_RESPONSE, data, call, t);
    }

    /**
     * 分发失败的结果
     *
     * @param code 错误类型
     * @param data
     * @param call
     * @param t
     */
    public void dispatchFailure(int code, FailureVo data, Call<T> call, Throwable t) {
        callback.dispatchFailure(code, data, call, t);
    }

}
