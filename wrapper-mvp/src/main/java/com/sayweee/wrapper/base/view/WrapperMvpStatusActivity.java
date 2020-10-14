package com.sayweee.wrapper.base.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;

import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.bean.BaseBean;
import com.sayweee.wrapper.bean.FailureBean;

import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusManager;
import me.winds.wrapper.status.StatusProvider;


/**
 * Author:  winds
 * Data:    2020/8/30
 * Version: 1.0
 * Desc:
 */
public abstract class WrapperMvpStatusActivity<P extends BaseMvpPresenter> extends WrapperMvpActivity<P> implements IStatus {

    protected StatusDelegate statusDelegate;

    @Override
    protected void setContentView() {
        super.setContentView();
        onStatusAttach(getView());
    }

    /**
     * 显示加载状态 如无需网络请求，请在耗时操作后主动移除此状态
     *
     * @param view
     * @param savedInstanceState
     */
    @CallSuper
    @Override
    public void initView(View view, Bundle savedInstanceState) {
        showLoadingStatus();
    }

    /**
     * 数据回调 默认在loading count == 0 时 移除状态布局
     *
     * @param url    请求的url
     * @param dataVo 解析成功后返回VO对象
     */
    @CallSuper
    @Override
    public void onResponse(String url, BaseBean dataVo) {
        onResponseStatus(url, dataVo);
    }

    /**
     * 加载失败 重写时需要调用removeStatus
     *
     * @param url
     * @param dataVo
     * @return
     */
    @Override
    public boolean onError(String url, FailureBean dataVo) {
        onErrorStatus(url, dataVo);
        return super.onError(url, dataVo);
    }

    @Override
    public void showLoadingStatus() {
        if (statusDelegate != null) {
            statusDelegate.showLoadingStatus();
        }
    }

    @Override
    public void onStatusAttach(View attachView) {
        statusDelegate = new StatusDelegate(attachView) {
            @Override
            protected boolean isRequestFinish() {
                return presenter == null || presenter.getRequestCount() <= 0;
            }
        };
    }

    @Override
    public void onStatusDetach() {
        removeStatus();
    }

    @Override
    public void showStatus(final View view, OnStatusListener listener) {
        if(view != null) {
            showStatus(view, view.getClass().getName() + System.currentTimeMillis(), listener);
        }
    }

    @Override
    public void showStatus(StatusProvider provider, OnStatusListener listener) {
        if (statusDelegate != null) {
            statusDelegate.showStatus(provider, listener);
        }
    }

    @Override
    public void showStatus(final View view, String status, OnStatusListener listener) {
        showStatus(new StatusProvider(status) {
            @Override
            public View getStatusView(ViewGroup containerView) {
                return view;
            }
        }, listener);
    }

    @Override
    public void removeStatus() {
        if (statusDelegate != null) {
            statusDelegate.removeStatus();
        }
    }

    @Override
    public void removeStatus(String status) {
        if (statusDelegate != null) {
            statusDelegate.removeStatus(status);
        }
    }

    @Override
    public boolean isStatusShow() {
        return statusDelegate != null && statusDelegate.getStatusManager().getCurrentStatusProvider() != null;
    }

    @Override
    public StatusManager getStatusManager() {
        return statusDelegate != null ? statusDelegate.getStatusManager() : null;
    }

    protected void onResponseStatus(String url, BaseBean dataVo) {
        if (statusDelegate != null) {
            statusDelegate.onResponseStatus();
        }
    }

    protected void onErrorStatus(String url, FailureBean dataVo) {
        if (statusDelegate != null) {
            statusDelegate.onErrorStatus();
        }
    }

    protected StatusDelegate getStatusDelegate() {
        return statusDelegate;
    }

}
