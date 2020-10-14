package com.sayweee.wrapper.base.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.core.model.BaseVo;
import com.sayweee.wrapper.core.model.FailureVo;

import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusProvider;

/**
 * Author:  winds
 * Data:    2020/8/30
 * Version: 1.0
 * Desc:   状态实现类
 */
public abstract class WrapperStatusFragment<P extends BaseMvpPresenter> extends WrapperMvpFragment<P> {
    StatusDelegate delegate;

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
        onStatusCreated();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 显示加载状态 如无需网络请求，请在耗时操作后主动移除此状态
     * @param savedInstanceState
     */
    @CallSuper
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        showLoadingStatus();
    }

    /**
     * 数据回调 默认在loading count == 0 时 移除状态布局
     * @param url    请求的url
     * @param dataVo 解析成功后返回VO对象
     */
    @CallSuper
    @Override
    public void onResponse(String url, BaseVo dataVo) {
        onResponseStatus(url, dataVo);
    }

    /**
     * 加载失败 重写时需要调用removeStatus
     * @param url
     * @param dataVo
     * @return
     */
    @Override
    public boolean onError(String url, FailureVo dataVo) {
        onErrorStatus(url, dataVo);
        return super.onError(url, dataVo);
    }

    protected void onStatusCreated() {
        createStatusDelegate(contentView);
    }

    protected void createStatusDelegate(View view) {
        if (delegate == null) {
            delegate = new StatusDelegate(view) {
                @Override
                public int getRequestCount() {
                    return presenter != null ? presenter.getRequestCount() : 0;
                }

                @Override
                public void load() {
                    if (isLazyLoad()) {
                        lazyLoadData();
                    } else {
                        loadData(null);
                    }
                }
            };
        }
    }

    public StatusDelegate getStatusDelegate() {
        return delegate;
    }

    public void showLoadingStatus() {
        if (delegate != null) {
            delegate.showLoadingStatus();
        }
    }

    public void onResponseStatus(String url, BaseVo dataVo){
        if (delegate != null) {
            delegate.onResponseStatus();
        }
    }

    protected void onErrorStatus(String url, FailureVo dataVo){
        if(delegate != null) {
            delegate.onErrorStatus(url, dataVo);
        }
    }

    protected void showStatus(StatusProvider provider, OnStatusListener listener){
        if(delegate != null) {
            delegate.showStatus(provider, listener);
        }
    }

    protected void removeStatus() {
        if(delegate != null) {
            delegate.removeStatus();
        }
    }

    protected void removeStatus(String status){
        if(delegate != null) {
            delegate.removeStatus(status);
        }
    }

    protected void showStatus(final View view, String status, OnStatusListener listener){
        showStatus(new StatusProvider(status) {
            @Override
            public View getStatusView(ViewGroup containerView) {
                return view;
            }
        }, listener);
    }

    protected abstract boolean isLazyLoad();
}
