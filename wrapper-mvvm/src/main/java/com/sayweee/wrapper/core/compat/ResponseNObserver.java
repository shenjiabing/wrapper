package com.sayweee.wrapper.core.compat;

import android.text.TextUtils;

import com.sayweee.wrapper.bean.FailureBean;
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
 * Desc:    推荐使用{@com.sayweee.core.compat.ResponseObserver}
 */
public abstract class ResponseNObserver<T extends N> extends DisposableObserver<T> {

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
        if (t != null) {
            onResponse(t);
        } else {
            onError(FailureBean.handle(false, "parse error", ExceptionHandler.handleException(ExceptionHandler.ERROR_PARSE, "parse error")));
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
