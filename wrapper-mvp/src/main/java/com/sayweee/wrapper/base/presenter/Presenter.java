package com.sayweee.wrapper.base.presenter;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public interface Presenter<V> {

    /**
     * 将Activity或者Fragment绑定到presenter
     */
    void attachView(V view);

    /**
     * 判断当前Activity或者Fragment是否已绑定到presenter
     *
     * @return
     */
    boolean isAttached();

    /**
     * 将Activity或者Fragment从presenter中分离出来
     */
    void detachView();
}
