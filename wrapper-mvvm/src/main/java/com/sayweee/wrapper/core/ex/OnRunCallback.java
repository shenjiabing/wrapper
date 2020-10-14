package com.sayweee.wrapper.core.ex;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/26.
 * Desc:
 */
public interface OnRunCallback<T> {

    void onStart();

    T onBackground() throws Exception;

    void onSuccess(T t);

    void onError(Throwable throwable);
}