package com.sayweee.wrapper.base.presenter;


import com.sayweee.logger.Logger;
import com.sayweee.wrapper.ManagerConfig;
import com.sayweee.wrapper.base.view.IMvpView;
import com.sayweee.wrapper.core.http.Callback;
import com.sayweee.wrapper.core.http.ResponseCallback;
import com.sayweee.wrapper.core.http.convert.HttpError;
import com.sayweee.wrapper.core.model.BaseVo;
import com.sayweee.wrapper.core.model.FailureCode;
import com.sayweee.wrapper.core.model.FailureVo;
import com.sayweee.wrapper.utils.Utils;
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

    protected boolean needDialog = true;
    public int requestCount;
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
                getView().onResponse(call.request().url().toString(), (BaseVo) response.body());
            }
            if (requestCount <= 0) {
                getView().onStopLoading();
            }
        }
    }

    @Override
    public void dispatchFailure(int code, FailureVo data, Call call, Throwable t) {
        Logger.error(t);
        if (isAttached()) {
            Logger.json(viewClassName, code, data, call.request().url().toString());
            requestCount--;
            getView().onStopLoading();
            boolean process = getView().onError(call.request().url().toString(), data); //是否已处理
            if (!process) {
                switch (code) {
                    case FailureCode.FAILURE_CONVERT:  //不支持的解析类型
                    case FailureCode.FAILURE_RESPONSE: //response 返回result = false
                    case FailureCode.FAILURE_NETWORK:  //网络问题
                    case FailureCode.FAILURE_RETROFIT: //retrofit主动failure
                    case FailureCode.FAILURE_SERVER:   //retrofit主动failure
                    default:
                        showToast(data.getMessage());
                        break;
                }
            }
        }
    }

    /**
     * 是否需要加载弹窗效果
     *
     * @return
     */
    @Override
    public boolean needLoading() {
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

    /**
     * 提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toaster.showToast(msg);
    }

    public <T> T getHttpService(Class<T> service) {
        return ManagerConfig.getInstance().getHttpService(service);
    }

    public <T> T getHttpService() {
        return ManagerConfig.getInstance().getHttpService();
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
        if (requestCount > 0 && bool && needLoading()) {
            getView().onLoading();
        }
        if (Utils.isNetworkConnected()) {
            call.enqueue(responseCallback);
        } else {
            dispatchFailure(FailureCode.FAILURE_RESPONSE, new FailureVo(HttpError.CODE_NETWORK_ERROR, false, "no network", null), call, null);
        }
    }

    /**
     * 设置请求
     *
     * @param call
     * @param <T>
     */
    public <T extends BaseVo> void enqueue(Call<T> call) {
        enqueue(true, call);
    }


    /**
     * 请求，同时设置本次请求是否需要加载效果
     *
     * @param bool
     * @param call
     * @param <T>
     */
    public <T extends BaseVo> void enqueue(boolean bool, Call<T> call) {
        enqueueCompat(bool, call);
    }


}