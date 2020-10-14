package com.sayweee.wrapper.base.presenter;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:
 */
public interface IMvpPresenter {

    boolean needLoading();

    void showLoading();

    void setShowLoading(boolean needLoading);

    int getRequestCount();
}
