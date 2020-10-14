package com.sayweee.wrapper.core.model;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:    数据层接口，用于和ViewModel层关联管理
 */
public interface IModel {

    /**
     * 当model层与view model建立关联时回调
     */
    void onAttach();

    /**
     * 当model层与view model断开关联时回调
     */
    void onDetach();

}
