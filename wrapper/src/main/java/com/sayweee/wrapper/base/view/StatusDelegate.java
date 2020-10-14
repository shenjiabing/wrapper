package com.sayweee.wrapper.base.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.sayweee.wrapper.R;
import com.sayweee.wrapper.core.model.FailureVo;

import me.winds.wrapper.status.DefaultStatus;
import me.winds.wrapper.status.OnDefaultStatusListener;
import me.winds.wrapper.status.OnStatusListener;
import me.winds.wrapper.status.StatusManager;
import me.winds.wrapper.status.StatusProvider;


/**
 * Author:  winds
 * Data:    2018/3/12
 * Version: 1.0
 * Desc:
 */
public abstract class StatusDelegate {

    protected StatusManager manager;

    public StatusDelegate(View view) {
        if (view != null) {
            init(view);
        }
    }
    /**
     * 初始化StatusManager
     *
     * @param view
     */
    public void init(View view) {
        manager = new StatusManager(view);
    }


    /**
     * 展示对应的状态布局
     *
     * @param status
     */
    public void showStatus(String status) {
        if (manager != null) {
            manager.show(status);
        }
    }

    public void showLoadingStatus() {
        showStatus(DefaultStatus.STATUS_LOADING);
    }

    public void showNetErrorStatus() {
        showStatus(DefaultStatus.STATUS_NO_NETWORK);
    }

    public void showEmptyStatus() {
        showStatus(DefaultStatus.STATUS_EMPTY);
    }

    /**
     * 展示对应StatusProvider的状态布局
     *
     * @param provider
     * @param listener
     */
    public void showStatus(StatusProvider provider, OnStatusListener listener) {
        if (manager != null) {
            manager.show(provider, listener);
        }
    }

    public void showStatus(final View view, String status, OnStatusListener listener) {
       showStatus(new StatusProvider(status) {
           @Override
           public View getStatusView(ViewGroup containerView) {
               return view;
           }
       }, listener);
    }

    public StatusManager getStatusManager() {
        return manager;
    }

    /**
     * 移除当前的状态布局
     */
    public void removeStatus() {
        removeStatus(null);
    }

    /**
     * 移除对应的状态布局
     *
     * @param status
     */
    public void removeStatus(String status) {
        if (manager != null) {
            if (TextUtils.isEmpty(status)) {
                manager.removeStatus();
            } else {
                manager.removeStatus(status);
            }
        }
    }

    /**
     * 正确做法是  先判断当前的状态是否时没有网络，再判断当前页面网络请求数量  若小于等于0时 调用此方法
     *
     * @param dataVo
     */
    public void onErrorStatus(String url, FailureVo dataVo) {
        removeStatus();
    }

    /**
     * 提供默認的處理manager處理方法，按界面需求重寫
     */
    public void onResponseStatus() {
        if (getRequestCount() <= 0) {
            removeStatus();
        }
    }

    /**
     * 此处理方法仅供参考  如使用 请覆盖重写此方法
     */
    public void processFailureErrorStatusDefault() {
        /**
         * 从loadData进入的方法
         * @see #loadData(Bundle)
         */
        manager.show(DefaultStatus.STATUS_LOAD_ERROR, new OnDefaultStatusListener() {
            @Override
            public void onStatusViewCreate(String status, View statusView) {
                statusView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.show(DefaultStatus.STATUS_LOADING);
                        load();
                    }
                });
            }
        });
    }

    /**
     * 处理网络异常问题时的状态显示
     * 此方法的回调仅实现重新初始化
     * 此处理方法仅供参考  如使用 请覆盖重写此方法
     */
    public void processNetErrorStatusDefault() {
        /**
         * 从loadData进入的方法
         * @see #loadData(Bundle)
         */
        manager.show(DefaultStatus.STATUS_NO_NETWORK, new OnDefaultStatusListener() {
            @Override
            public void onStatusViewCreate(String status, View statusView) {
                statusView.findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        manager.show(DefaultStatus.STATUS_LOADING);
                        load();
                    }
                });
            }
        });
    }

    /**
     * 获取当前剩下的加载次数 用于是否隐藏当前的状态布局
     *
     * @return 当返回小于等于0时  隐藏状态布局
     */
    public abstract int getRequestCount();

    /**
     * 此方法用于在点击重试时重新加载数据
     */
    public abstract void load();
}
