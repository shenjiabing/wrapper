package com.sayweee.wrapper.base.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/28.
 * Desc:
 */
public interface IView {

    /**
     * 获取当前的布局
     * @return
     */
    int getLayoutRes();

    /**
     * 初始化当前view
     * @param view
     * @param savedInstanceState
     */
    void initView(View view, Bundle savedInstanceState);

    /**
     * 加载数据
     */
    void loadData();

    /**
     * 获取当前的view
     * @return
     */
    View getView();

    /**
     * 获取当前{@com.sayweee.wrapper.base.view.IView#getLayoutRes()}返回的view
     * @return
     */
    View getContentView();
}
