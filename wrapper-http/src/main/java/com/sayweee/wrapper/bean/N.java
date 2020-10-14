package com.sayweee.wrapper.bean;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:    所有继承此接口 默认不处理 直接回调回原始数据
 */
public interface N {

    /**
     * 设置原始数据
     * @param raw
     */
    void setRawData(String raw);

}
