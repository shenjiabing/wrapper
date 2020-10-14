package com.sayweee.wrapper.http;

import android.app.Application;
import android.content.Context;

import com.sayweee.wrapper.http.converter.WrapperConverterFactory;
import com.sayweee.wrapper.http.cookie.CookieJarImpl;
import com.sayweee.wrapper.http.cookie.CookieStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/29.
 * Desc:
 */
public class RetrofitIml implements IRetrofit {
    protected int timeout = 15;
    protected boolean logEnable = true;
    protected Retrofit retrofit;
    protected Application application;
    protected Map<String, Object> services = new HashMap();

    public static RetrofitIml get() {
        return Builder.instance;
    }

    public final static class Builder {
        private static RetrofitIml instance = new RetrofitIml();
    }

    @Override
    public RetrofitIml attach(Application application) {
        this.application = application;
        return this;
    }

    @Override
    public RetrofitIml detach() {
        cancelAll();
        return this;
    }

    @Override
    public <T> T getHttpService(Class<T> clazz) {
        if (!services.containsKey(clazz.getName())) {
            services.put(clazz.getName(), createNewService(clazz));
        }
        return (T) services.get(clazz.getName());
    }

    @Override
    public RetrofitIml initHttp(Retrofit retrofit) {
        this.retrofit = retrofit;
        return this;
    }

    @Override
    public RetrofitIml initHttp(String host, CookieStore cookieStore, Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        //全局的写入超时时间
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        //全局的连接超时时间
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        //设置cookie
        if (cookieStore != null) {
            builder.cookieJar(new CookieJarImpl(cookieStore));
        }
        if (interceptors != null && interceptors.length > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        ArrayList<Converter.Factory> converters = new ArrayList<>();
        converters.add(WrapperConverterFactory.create());

        ArrayList<CallAdapter.Factory> adapters = new ArrayList<>();
        adapters.add(RxJava2CallAdapterFactory.create());

        return initHttp(builder.build(), host, null, adapters, converters);
    }

    @Override
    public RetrofitIml initHttp(OkHttpClient client, String host, Call.Factory factory, List<CallAdapter.Factory> adapters, List<Converter.Factory> converters) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(host);
        builder.client(client);
        if (factory != null) {
            builder.callFactory(factory);
        }
        if (adapters != null && adapters.size() > 0) {
            for (CallAdapter.Factory adapter : adapters) {
                builder.addCallAdapterFactory(adapter);
            }
        }
        if (converters != null && converters.size() > 0) {
            for (Converter.Factory converter : converters) {
                builder.addConverterFactory(converter);
            }
        }
        retrofit = builder.build();
        return this;
    }

    @Override
    public void cancelAll() {
        cancelAll(getOkHttpClient());
    }

    @Override
    public CookieJar getCookieJar() {
        OkHttpClient client = getOkHttpClient();
        return client != null ? client.cookieJar() : null;
    }

    @Override
    public OkHttpClient getOkHttpClient() {
        return retrofit != null ? (OkHttpClient) retrofit.callFactory() : null;
    }

    @Override
    public Retrofit getRetrofitClient() {
        return retrofit;
    }

    @Override
    public IRetrofit clearCache() {
        services.clear();
        return this;
    }

    @Override
    public void cancelTag(Object tag) {
        cancelTag(getOkHttpClient(), tag);
    }

    @Override
    public void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) {
            return;
        }
        try {
            for (Call call : client.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : client.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void cancelAll(OkHttpClient client) {
        if (client == null) {
            return;
        }
        try {
            for (Call call : client.dispatcher().queuedCalls()) {
                call.cancel();
            }
            for (Call call : client.dispatcher().runningCalls()) {
                call.cancel();
            }
        } catch (Exception e) {
        }
    }

    protected <T> T createNewService(Class<T> clazz) {
        return retrofit != null && clazz != null ? retrofit.create(clazz) : null;
    }

    public Retrofit.Builder setHttpClient(String host, int timeout, CookieStore cookieStore, Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        //全局的写入超时时间
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        //全局的连接超时时间
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        //设置cookie
        if (cookieStore != null) {
            builder.cookieJar(new CookieJarImpl(cookieStore));
        }
        if (interceptors != null && interceptors.length > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return new Retrofit.Builder().client(builder.build()).baseUrl(host);
    }

    public RetrofitIml setTimeout(int seconds) {
        this.timeout = seconds;
        return this;
    }

    public void setLogEnable(boolean enable) {
        this.logEnable = enable;
    }

    public boolean isLogEnable() {
        return logEnable;
    }

    public Context getAttachContext() {
        return application;
    }
}
