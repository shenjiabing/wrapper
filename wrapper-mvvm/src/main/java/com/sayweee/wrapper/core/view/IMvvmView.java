package com.sayweee.wrapper.core.view;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/28.
 * Desc:
 */
public interface IMvvmView {

    /**
     * 创建view model
     *
     * @param <VM>
     * @return
     */
    <VM> VM createModel();

    /**
     * view层关联view model
     */
    void injectModel();

    /**
     * 关联后回调
     */
    void attachModel();

}
