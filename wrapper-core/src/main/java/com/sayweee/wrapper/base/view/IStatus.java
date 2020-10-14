package com.sayweee.wrapper.base.view;

import android.view.View;

import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusManager;
import me.winds.wrapper.status.StatusProvider;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/30.
 * Desc:
 */
public interface IStatus {

    void onStatusAttach(View attachView);

    void onStatusDetach();

    void showStatus(View view, OnStatusListener listener);

    void showStatus(StatusProvider provider, OnStatusListener listener);

    void showStatus(View view, String status,  OnStatusListener listener);

    void removeStatus();

    void removeStatus(String status);

    void showLoadingStatus();

    boolean isStatusShow();

    StatusProvider getCurrentStatusProvider();

    StatusManager getStatusManager();
}
