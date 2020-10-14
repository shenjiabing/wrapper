package com.sayweee.wrapper.core.view;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/13.
 * Desc:
 */
public interface IBindingView {

    /**
     * view层binding
     */
    void injectBinding();

    /**
     * 是否使用data binding
     *
     * @return
     */
    boolean useDataBinding();
}
