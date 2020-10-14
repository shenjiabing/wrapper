package com.sayweee.wrapper;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.sayweee.scheduler.SafeDispatchHandler;
import com.sayweee.wrapper.core.http.DomainCallFactory;
import com.sayweee.wrapper.core.http.HttpLoggingInterceptor;
import com.sayweee.wrapper.core.http.convert.CommonConverterFactory;
import com.sayweee.wrapper.core.http.cookie.CookieJarImpl;
import com.sayweee.wrapper.core.http.cookie.CookieStore;
import com.sayweee.wrapper.core.http.cookie.SPCookieStore;
import com.sayweee.wrapper.utils.CrashHandler;
import com.sayweee.wrapper.widget.Toaster;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Author:  winds
 * Data:    2018/3/12
 * Version: 1.0
 * Desc:
 */
public class ManagerConfig {
    private Application context;
    private Handler mMainHandler;
    private Retrofit retrofit;
    private String replaceHost;
    private Map<String, Object> services = new HashMap();

    private ManagerConfig() {
        mMainHandler = new SafeDispatchHandler(Looper.getMainLooper());
    }


    private static class ConfigBuilder {
        private static ManagerConfig holder = new ManagerConfig();
    }

    /**
     * 获取实例化的方法  第一次实例化 请在application   同时调用init方法
     *
     * @return
     */
    public static ManagerConfig getInstance() {
        return ConfigBuilder.holder;
    }

    /**
     * 第一次实例化调用
     *
     * @param app
     * @return
     */
    public ManagerConfig attach(Application app) {
        context = app;
        attach();
        return this;
    }

    private void attach() {
        CrashHandler.install();
        Toaster.attach(context);
    }

    /**
     * 获取Application对象
     *
     * @return
     */
    public Application getApplicationContext() {
        checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        return context;
    }

    /**
     * 获取主线程handler
     *
     * @return
     */
    public Handler getMainThreadHandler() {
        checkNotNull(context, "please call ManagerConfig.getInstance().init() first in application!");
        return mMainHandler;
    }

    /**
     * 获取主线程threadId
     *
     * @return
     */
    public long getMainThreadId() {
        return getMainThreadHandler().getLooper().getThread().getId();
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
    public ManagerConfig initHttpClient(String host) {
        return initHttpClient(host, new HttpLoggingInterceptor(HttpLoggingInterceptor.Level.BODY));
    }


    public ManagerConfig initHttpClient(String host, Interceptor... interceptors) {
        return initHttpClient(host, new SPCookieStore(context), interceptors);
    }

    /**
     * 初始化okhttp默认提供通用实现
     *
     * @return
     */
    public ManagerConfig initHttpClient(String host, CookieStore cookieStore, Interceptor... interceptors) {
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

    DomainCallFactory factory;
    /**
     * 初始化retrofit  根据需求定制httpclient
     *
     * @param client
     * @return
     */
    public ManagerConfig initRetrofit(OkHttpClient client, String host) {
        factory = new DomainCallFactory(client);
        retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .callFactory(factory)
                .addConverterFactory(CommonConverterFactory.create())
                .build();
        return this;
    }

    public DomainCallFactory getDomainFactory() {
        return factory;
    }


    public ManagerConfig replaceRequestHost(String host) {
        this.replaceHost = host;
        return this;
    }


    public <T> ManagerConfig createService(Class<T> clazz) {
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
}
