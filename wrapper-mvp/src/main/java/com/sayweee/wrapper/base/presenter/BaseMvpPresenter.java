package com.sayweee.wrapper.base.presenter;


import com.sayweee.logger.Logger;
import com.sayweee.wrapper.base.view.IMvpView;

import com.sayweee.wrapper.bean.BaseBean;
import com.sayweee.wrapper.bean.FailureBean;
import com.sayweee.wrapper.http.Callback;
import com.sayweee.wrapper.http.ExceptionHandler;
import com.sayweee.wrapper.http.ResponseCallback;
import com.sayweee.wrapper.http.RetrofitIml;
import com.sayweee.wrapper.http.support.Utils;
import com.sayweee.wrapper.widget.Toaster;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public class BaseMvpPresenter extends MvpPresenter<IMvpView> implements IMvpPresenter, Callback {

    public int requestCount;
    protected boolean needDialog = true;
    private ResponseCallback responseCallback;

    public BaseMvpPresenter() {
        responseCallback = new ResponseCallback(this);
    }

    @Override
    public void dispatchSuccess(Call call, Response response) {
        if (isAttached()) {
            requestCount--;
            getView().beforeSuccess();
            boolean process = getView().onResponse(call, response);
            if (!process) {
                getView().onResponse(call.request().url().toString(), (BaseBean) response.body());
            }
            if (requestCount <= 0) {
                getView().onStopLoading();
            }
        }
    }

    @Override
    public void dispatchFailure(int code, FailureBean data, Call call, Throwable t) {
        Logger.error(t);
        if (isAttached()) {
            requestCount--;
            getView().onStopLoading();
            boolean process = getView().onError(call.request().url().toString(), data); //是否已处理
            if (!process) {
                processFailure(code, data, call, t);

            }
        }
    }

    /**
     * 是否需要加载弹窗效果
     *
     * @return
     */
    @Override
    public boolean needShowLoading() {
        return needDialog;
    }

    @Override
    public void showLoading() {
        getView().onLoading();
    }

    /**
     * 设置请求时是否需要加载进度条   需要在请求前设置
     *
     * @param needLoading
     */
    @Override
    public void setShowLoading(boolean needLoading) {
        this.needDialog = needLoading;
    }

    @Override
    public int getRequestCount() {
        return requestCount;
    }

    protected void processFailure(int code, FailureBean data, Call call, Throwable t) {
        showToast(data.getMessage());
    }

    /**
     * 提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toaster.showToast(msg);
    }

    public <T> T getHttpService(Class<T> service) {
        return RetrofitIml.get().getHttpService(service);
    }

    /**
     * 兼容原有请求
     *
     * @param call
     */
    public void enqueueCompat(Call call) {
        enqueue(true, call);
    }

    public void enqueueCompat(boolean bool, Call call) {
        requestCount++;
        if (requestCount > 0 && bool && needShowLoading()) {
            getView().onLoading();
        }
        if (Utils.isNetworkConnected(RetrofitIml.get().getAttachContext())) {
            call.enqueue(responseCallback);
        } else {
            dispatchFailure(ExceptionHandler.ERROR_NETWORK, new FailureBean(false, "no network", ExceptionHandler.handleException(ExceptionHandler.ERROR_NETWORK, "no network")), call, null);
        }
    }

    /**
     * 设置请求
     *
     * @param call
     * @param <T>
     */
    private  <T extends BaseBean> void enqueue(Call<T> call) {
        enqueue(true, call);
    }


    /**
     * 请求，同时设置本次请求是否需要加载效果
     *
     * @param bool
     * @param call
     * @param <T>
     */
    private <T extends BaseBean> void enqueue(boolean bool, Call<T> call) {
        enqueueCompat(bool, call);
    }
}