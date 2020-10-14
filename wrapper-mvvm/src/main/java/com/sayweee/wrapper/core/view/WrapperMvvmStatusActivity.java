package com.sayweee.wrapper.core.view;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.sayweee.wrapper.base.view.IStatus;
import com.sayweee.wrapper.base.view.delegate.StatusDelegate;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.core.model.ILoaderModel;

import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusManager;
import me.winds.wrapper.status.StatusProvider;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/14.
 * Desc:
 */
public abstract class WrapperMvvmStatusActivity<VM extends BaseViewModel<? extends ILoaderModel>> extends WrapperMvvmActivity<VM> implements IStatus {

    StatusDelegate statusDelegate;

    @Override
    protected void setContentView() {
        super.setContentView();
        onStatusAttach(getContentView());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStatusDetach();
    }

    @Override
    public void onStatusAttach(View attachView) {
        statusDelegate = new StatusDelegate();
        statusDelegate.onStatusAttach(attachView);
    }

    @Override
    public void onStatusDetach() {
        if (statusDelegate != null) {
            statusDelegate.onStatusDetach();
        }
    }

    @Override
    public void showStatus(View view, OnStatusListener listener) {
        if (statusDelegate != null) {
            statusDelegate.showStatus(view, listener);
        }
    }

    @Override
    public void showStatus(StatusProvider provider, OnStatusListener listener) {
        if (statusDelegate != null) {
            statusDelegate.showStatus(provider, listener);
        }
    }

    @Override
    public void showStatus(View view, String status, OnStatusListener listener) {
        if (statusDelegate != null) {
            statusDelegate.showStatus(view, status, listener);
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
    public boolean isStatusShow() {
        return statusDelegate != null && statusDelegate.isStatusShow();
    }

    @Override
    public StatusProvider getCurrentStatusProvider() {
        return statusDelegate != null ? statusDelegate.getCurrentStatusProvider() : null;
    }

    @Override
    public StatusManager getStatusManager() {
        return statusDelegate != null ? statusDelegate.getStatusManager() : null;
    }
}
