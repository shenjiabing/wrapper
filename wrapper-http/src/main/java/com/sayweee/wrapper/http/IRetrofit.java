package com.sayweee.wrapper.http;

import com.sayweee.wrapper.http.cookie.CookieStore;

import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/29.
 * Desc:
 */
public interface IRetrofit extends IHttp {

    /**
     * 根据当前的class生成服务
     * 需保证retrofit初始化后调用
     *
     * @param clazz
     * @param <S>
     * @return
     */
    <S> S getHttpService(Class<S> clazz);

    /**
     * 初始化retrofit
     *
     * @param retrofit
     * @return
     */
    IRetrofit initHttp(Retrofit retrofit);

    /**
     * 初始化retrofit
     *
     * @param host
     * @param cookieStore
     * @param interceptors
     * @return
     */
    IRetrofit initHttp(String host, CookieStore cookieStore, Interceptor... interceptors);

    /**
     * 初始化retrofit
     *
     * @param client
     * @param host
     * @param factory
     * @param adapters
     * @param converters
     * @return
     */
    IRetrofit initHttp(OkHttpClient client, String host, okhttp3.Call.Factory factory, List<CallAdapter.Factory> adapters, List<Converter.Factory> converters);

    /**
     * 获取retrofit实例
     *
     * @return
     */
    Retrofit getRetrofitClient();

    /**
     * 清除缓存，用于重置前
     * @return
     */
    IRetrofit clearCache();
}
