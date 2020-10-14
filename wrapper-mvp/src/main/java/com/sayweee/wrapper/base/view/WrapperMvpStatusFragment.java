package com.sayweee.wrapper.base.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

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
 * Desc:   状态实现类
 */
public abstract class WrapperMvpStatusFragment<P extends BaseMvpPresenter> extends WrapperMvpFragment<P> implements IStatus {

    protected StatusDelegate statusDelegate;

    /**
     * 为status页面提供父布局 非一般情况 请避免重写此方法
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout parent = new FrameLayout(activity);
        parent.addView(super.onCreateView(inflater, container, savedInstanceState));
        return parent;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        onStatusAttach(view);
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 显示加载状态 如无需网络请求，请在耗时操作后主动移除此状态
     *
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
    public void onStatusAttach(View attachView) {
        if (statusDelegate == null) {
            statusDelegate = new StatusDelegate(attachView) {
                @Override
                protected boolean isRequestFinish() {
                    return presenter == null || presenter.getRequestCount() <= 0;
                }
            };
        }
    }

    @Override
    public void showStatus(View view, OnStatusListener listener) {
        if (view != null) {
            showStatus(view, view.getClass().getName() + System.currentTimeMillis(), listener);
        }
    }

    @Override
    public void showStatus(final View view, String status, OnStatusListener listener) {
        showStatus(new StatusProvider(status == null ? String.valueOf(System.currentTimeMillis()) : status) {
            @Override
            public View getStatusView(ViewGroup containerView) {
                return view;
            }
        }, listener);
    }

    @Override
    public void showStatus(StatusProvider provider, OnStatusListener listener) {
        if (statusDelegate != null) {
            statusDelegate.showStatus(provider, listener);
        }
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
    public void showLoadingStatus() {
        if (statusDelegate != null) {
            statusDelegate.showLoadingStatus();
        }
    }

    @Override
    public void onStatusDetach() {
        removeStatus();
    }

    @Override
    public boolean isStatusShow() {
        return statusDelegate != null && statusDelegate.getStatusManager().getCurrentStatusProvider() != null;
    }

    @Override
    public StatusManager getStatusManager() {
        return statusDelegate != null ? statusDelegate.getStatusManager() : null;
    }

    public void onResponseStatus(String url, BaseBean dataVo) {
        if (statusDelegate != null) {
            statusDelegate.onResponseStatus();
        }
    }

    protected void onErrorStatus(String url, FailureBean dataVo) {
        if (statusDelegate != null) {
            statusDelegate.onErrorStatus();
        }
    }

    public StatusDelegate getStatusDelegate() {
        return statusDelegate;
    }

    protected abstract boolean isLazyLoad();
}
