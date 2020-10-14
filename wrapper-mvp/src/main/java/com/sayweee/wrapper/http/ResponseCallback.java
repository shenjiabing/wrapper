package com.sayweee.wrapper.http;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.bean.BaseBean;
import com.sayweee.wrapper.bean.FailureBean;
import com.sayweee.wrapper.http.converter.WrapperResponseConverter;

import java.lang.reflect.Field;

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
            dispatchFailure(response.code(), FailureBean.handle(false, "request failed",  ExceptionHandler.handleException(ExceptionHandler.ERROR_UNKNOWN, "request failed")),  call, null);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Logger.error(t);

        if(t instanceof ResponseException) {
            dispatchFailureResponse(new FailureBean(false, null, (ResponseException) t), call, t);
        }else{
            dispatchFailureResponse(new FailureBean(false, null, ExceptionHandler.handleException(t)), call, t);
        }
    }


    void dispatchResponse(Call<T> call, Response<T> response) {
        T body = response.body();
        if (body instanceof BaseBean) {
            dispatchSuccess(call, response);
            return;
        }
        try {
            if (body != null) {
                Field field = body.getClass().getDeclaredField(WrapperResponseConverter.resultName);
                field.setAccessible(true);
                boolean result = field.getBoolean(body);
                if (result) {
                    dispatchSuccess(call, response);
                } else {
                    String message = null;
                    try {
                        Field messageField = body.getClass().getDeclaredField(WrapperResponseConverter.messageName);
                        field.setAccessible(true);
                        message = String.valueOf(messageField.get(body));
                    } catch (Exception e) {

                    }
                    FailureBean data = new FailureBean( false, message, ExceptionHandler.handleException(ExceptionHandler.ERROR_RESPONSE_STATUS_FAILED, "response status failure"));
                    dispatchFailureResponse(data, call, null); //请求result = false
                }
                return;
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        FailureBean data = new FailureBean(false, "response parse error",  ExceptionHandler.handleException(ExceptionHandler.ERROR_PARSE, "response parse error"));
        dispatchFailureResponse(data, call, null);
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

    public void dispatchFailureResponse(FailureBean data, Call<T> call, Throwable t) {
        dispatchFailure(data.getErrorCode(), data, call, t != null ? t : data.getException());
    }

    /**
     * 分发失败的结果
     *
     * @param code 错误类型
     * @param data
     * @param call
     * @param t
     */
    public void dispatchFailure(int code, FailureBean data, Call<T> call, Throwable t) {
        callback.dispatchFailure(code, data, call, t);
    }

}
