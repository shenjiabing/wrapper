package com.sayweee.wrapper.http.support;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:    代理{@link Call.Factory} 拦截{@link #newCall(Request)}方法
 */
public abstract class CallFactoryProxy implements Call.Factory {

    protected final Call.Factory delegate;

    public CallFactoryProxy(Call.Factory delegate) {
        this.delegate = delegate;
    }
}
