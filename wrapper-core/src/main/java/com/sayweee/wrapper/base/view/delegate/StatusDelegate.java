package com.sayweee.wrapper.base.view.delegate;

import android.view.View;
import android.view.ViewGroup;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.base.view.IStatus;

import me.winds.wrapper.status.DefaultStatus;
import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusManager;
import me.winds.wrapper.status.StatusProvider;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/14.
 * Desc:
 */
public class StatusDelegate implements IStatus {

    StatusManager manager;

    @Override
    public void onStatusAttach(View attachView) {
        if (attachView != null) {
            manager = new StatusManager(attachView);
        } else {
            Logger.error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >>> the attached view is null! status manager generate failed"));
        }
    }

    @Override
    public void onStatusDetach() {
        removeStatus();
    }

    @Override
    public void showStatus(View view, OnStatusListener listener) {
        showStatus(view, String.valueOf(System.currentTimeMillis()), listener);
    }

    @Override
    public void showStatus(StatusProvider provider, OnStatusListener listener) {
        if (manager != null) {
            manager.show(provider, listener);
        }
    }

    @Override
    public void showStatus(final View view, String status, OnStatusListener listener) {
        if (status == null) {
            status = String.valueOf(System.currentTimeMillis());
        }
        if (view != null) {
            showStatus(new StatusProvider(status) {
                @Override
                public View getStatusView(ViewGroup containerView) {
                    return view;
                }
            }, listener);
        } else {
            Logger.error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >>> the status view is null!"));
        }
    }

    @Override
    public void removeStatus() {
        if(manager != null){
            manager.removeStatus();
        }
    }

    @Override
    public void removeStatus(String status) {
        if(manager != null){
            manager.removeStatus(status);
        }
    }

    @Override
    public void showLoadingStatus() {
        if(manager != null) {
            manager.show(DefaultStatus.STATUS_LOADING);
        }
    }

    @Override
    public boolean isStatusShow() {
        return manager != null && manager.getCurrentStatusProvider() != null && manager.getCurrentStatusProvider().isShow();
    }

    @Override
    public StatusProvider getCurrentStatusProvider() {
        return manager != null ? manager.getCurrentStatusProvider() : null;
    }

    @Override
    public StatusManager getStatusManager() {
        return manager;
    }
}
