package com.sayweee.wrapper.core.http;

import android.content.Context;

import androidx.annotation.Nullable;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.ManagerConfig;
import com.sayweee.wrapper.core.http.convert.CommonConverterFactory;
import com.sayweee.wrapper.core.http.cookie.CookieJarImpl;
import com.sayweee.wrapper.core.http.cookie.CookieStore;
import com.sayweee.wrapper.core.http.cookie.SPCookieStore;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:
 */
public class HttpManager {
    private Context context;
    private Retrofit retrofit;
    private String replaceHost;
    private Map<String, Object> services = new HashMap();

    public void attach(Context context) {
        this.context = context;
    }

    /**
     * 获取通用host 在未设置时返回空
     *
     * @return
     */
    public String getBaseHost() {
        return checkNotNull(retrofit, "please call ManagerConfig.getInstance().initHttpClient() first in application!")
                .baseUrl()
                .host();
    }

    public OkHttpClient getHttpClient() {
        return (OkHttpClient) checkNotNull(retrofit, "please call ManagerConfig.getInstance().initHttpClient() first in application!")
                .callFactory();
    }

    public Retrofit getRetrofitClient() {
        return retrofit;
    }

    /**
     * 初始化okhttp 默认提供通用实现
     *
     * @return
     */
    public HttpManager initHttpClient(String host) {
        return initHttpClient(host, new HttpLoggingInterceptor(HttpLoggingInterceptor.Level.BODY));
    }


    public HttpManager initHttpClient(String host, Interceptor... interceptors) {
        return initHttpClient(host, new SPCookieStore(context), interceptors);
    }

    /**
     * 初始化okhttp默认提供通用实现
     *
     * @return
     */
    public HttpManager initHttpClient(String host, CookieStore cookieStore, Interceptor... interceptors) {
        checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //全局的读取超时时间
        builder.readTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        //设置cookie
        if (cookieStore != null) {
            builder.cookieJar(new CookieJarImpl(cookieStore));
        }

        if (interceptors != null && interceptors.length > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return initRetrofit(builder.build(), host);
    }


    /**
     * 初始化retrofit  根据需求定制httpclient
     *
     * @param client
     * @return
     */
    public HttpManager initRetrofit(OkHttpClient client, String host) {
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(client)
//                .callFactory(new DomainCallFactory(client, host))
//                .callFactory(new ReplaceUrlCallFactory(client) {
//                    @Nullable
//                    @Override
//                    protected HttpUrl getNewUrl(Request request) {
//                        if (replaceHost != null) { //修改了host
//                            String old = request.url().toString();
//                            if (old != null && !old.contains(replaceHost)) {
//                                //替换了host
//                                String url = old.replace(getBaseHost(), replaceHost);
//                                return HttpUrl.get(URI.create(url));
//                            }
//                        }
//                        return null;
//                    }
//                })
                .addConverterFactory(CommonConverterFactory.create())
                .build();
        return this;
    }


    public HttpManager replaceRequestHost(String host) {
        this.replaceHost = host;
        return this;
    }


    public <T> HttpManager createService(Class<T> clazz) {
        T service = createNewService(clazz);
        services.put(ManagerConfig.class.getName(), service); //用于直接添加
        services.put(clazz.getName(), service);
        return this;
    }

    private <T> T createNewService(Class<T> service) {
        return checkNotNull(retrofit, "please call ManagerConfig.getInstance().initHttpClient() first in application!")
                .create(service);
    }

    /**
     * 获取默认设置的主service
     *
     * @param <T>
     * @return
     */
    public <T> T getHttpService() {
        if (services.containsKey(ManagerConfig.class.getName())) {
            return (T) services.get(ManagerConfig.class.getName());
        }
        throw new NullPointerException("please call create createService first");
    }

    public <T> T getHttpService(Class<T> service) {
        if (!services.containsKey(service.getName())) {
            services.put(service.getName(), createNewService(service));
        }
        return (T) services.get(service.getName());
    }

    private <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    /**
     * 根据tag取消网络请求
     *
     * @param tag
     */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        try {
            OkHttpClient client = ManagerConfig.getInstance().getHttpClient();
            cancelTag(client, tag);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
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
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}