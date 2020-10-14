package com.sayweee.wrapper.base.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;

import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.core.model.BaseVo;
import com.sayweee.wrapper.core.model.FailureVo;

import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusProvider;


/**
 * Author:  winds
 * Data:    2020/8/30
 * Version: 1.0
 * Desc:
 */
public abstract class WrapperStatusActivity<P extends BaseMvpPresenter> extends WrapperMvpActivity<P> {
    StatusDelegate delegate;

    @Override
    protected void initContentView() {
        super.initContentView();
        onStatusCreated();
    }

    /**
     * 显示加载状态 如无需网络请求，请在耗时操作后主动移除此状态
     * @param savedInstanceState
     * @param intent
     */
    @CallSuper
    @Override
    protected void initView(Bundle savedInstanceState, Intent intent) {
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
        createStatusDelegate(getContentView());
    }

    protected void createStatusDelegate(View view) {
        delegate = new StatusDelegate(view) {
            @Override
            public int getRequestCount() {
                return presenter == null ? 0 : presenter.getRequestCount();
            }

            @Override
            public void load() {
                loadData(null, getIntent());
            }
        };
    }

    protected StatusDelegate getStatusDelegate() {
        return delegate;
    }

    protected void showLoadingStatus() {
        if(delegate != null) {
            delegate.showLoadingStatus();
        }
    }

    protected void onResponseStatus(String url, BaseVo dataVo) {
        if(delegate != null) {
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
}
