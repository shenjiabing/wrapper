package com.sayweee.wrapper.core.compat;

import android.text.TextUtils;

import com.sayweee.wrapper.bean.FailureBean;
import com.sayweee.wrapper.bean.ResponseBean;
import com.sayweee.wrapper.http.ExceptionHandler;
import com.sayweee.wrapper.http.RetrofitIml;
import com.sayweee.wrapper.bean.N;
import com.sayweee.wrapper.http.support.Utils;
import com.sayweee.logger.Logger;
import com.sayweee.wrapper.widget.Toaster;

import io.reactivex.observers.DisposableObserver;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:    Response回调预处理
 */
public abstract class ResponseObserver<T> extends DisposableObserver<T> {

    private boolean useStrictMode;

    public ResponseObserver() {
        this(true);
    }

    public ResponseObserver(boolean useStrictMode) {
        this.useStrictMode = useStrictMode;
    }

    @Override
    protected final void onStart() {
        super.onStart();
        onBegin();
        if (!Utils.isNetworkConnected(RetrofitIml.get().getAttachContext())) {
            onError(FailureBean.handle(false, "network not connect", ExceptionHandler.handleException(ExceptionHandler.ERROR_NETWORK, "network not connect")));
            onFinish();
            if (!isDisposed()) {
                dispose();
            }
        }
    }

    @Override
    public final void onNext(T t) {
        if (t == null) { //解析出错
            onError(FailureBean.handle(true, "response is null", ExceptionHandler.handleException(ExceptionHandler.ERROR_PARSE, "response is null")));
        } else {
            if (N.class.isAssignableFrom(t.getClass())) { //继承自N
                onResponse(t);
            } else if (t instanceof ResponseBean) { //继承ResponseBean
                ResponseBean bean = (ResponseBean) t;
                if (bean.isSuccess()) {
                    if (bean.getData() != null || !useStrictMode) { //data不为空或者非严格模式
                        onResponse(t);
                    } else { //严格模式下 data = null
                        onError(FailureBean.handle(true, bean.message, ExceptionHandler.handleException(ExceptionHandler.ERROR_RESPONSE_DATA_NULL, "response data is null")));
                    }
                } else { //请求返回状态false
                    onError(FailureBean.handle(false, bean.message, ExceptionHandler.handleException(ExceptionHandler.ERROR_RESPONSE_STATUS_FAILED, "response status is failed")));
                }
            } else { //非约定解析实现 自行处理
                onResponse(t);
            }
        }
    }

    @Override
    public final void onError(Throwable e) {
        onError(FailureBean.handle(false, "", ExceptionHandler.handleException(e)));
        onFinish();
    }

    @Override
    public final void onComplete() {
        onFinish();
    }

    public void onBegin() {

    }

    /**
     * response的回调
     *
     * @param response
     */
    public abstract void onResponse(T response);

    /**
     * @param failure
     */
    public void onError(FailureBean failure) {
        Logger.json(failure);
        if (failure.getException() != null) {
            Logger.error(failure.getException());
        }
        //通用error处理
        if (failure != null && !TextUtils.isEmpty(failure.getMessage())) {
            Toaster.showToast(failure.getMessage());
        }
    }

    public void onFinish() {

    }

}
