package com.sayweee.wrapper.core.model;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:    网络请求
 */
public interface ILoaderModel<S> extends IModel {

    /**
     * 自动绑定当前的服务
     * @return
     */
    S inject();

    /**
     * 用于主动注入当前服务
     * @param clazz
     * @return
     */
    void inject(Class<?> clazz);

    /**
     * 获取当前的服务
     * @return
     */
    S getHttpService();

    /**
     * 创建新的服务
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T createHttpService(Class<T> clazz);

}
