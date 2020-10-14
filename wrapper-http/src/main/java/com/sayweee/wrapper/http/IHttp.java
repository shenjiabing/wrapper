package com.sayweee.wrapper.http;

import android.app.Application;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/29.
 * Desc:
 */
public interface IHttp {

    /**
     * 依附当前的生命周期
     * @param application
     * @return
     */
    IHttp attach(Application application);

    /**
     * 销毁时调用
     * @return
     */
    IHttp detach();

    /**
     * 获取cookieJar
     * @return
     */
    CookieJar getCookieJar();

    /**
     * 获取okhttp client
     * @return
     */
    OkHttpClient getOkHttpClient();

    /**
     * 取消所有请求请求
     */
    void cancelAll();

    /**
     * 取消所有请求请求
     * @param client
     */
    void cancelAll(OkHttpClient client);

    /**
     * 根据tag取消网络请求
     *
     * @param tag
     */
    void cancelTag(Object tag);

    /**
     * 根据Tag取消请求
     */
    void cancelTag(OkHttpClient client, Object tag);

}
