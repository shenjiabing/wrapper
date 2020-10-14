package com.sayweee.wrapper.http.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/25.
 * Desc:    通用header拦截器
 */
public class HeaderInterceptor implements Interceptor {

    protected boolean isAddMode;
    protected Map<String, String> headers = new HashMap<>();

    public HeaderInterceptor() {
    }

    public HeaderInterceptor(Map<String, String> headers) {
        setCommonHeaders(headers);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String key : keys) {
                String value = headers.get(key);
                if (isAddMode) {
                    builder.addHeader(key, value);
                } else {
                    builder.header(key, value);
                }
            }
        }
        return chain.proceed(builder.build());
    }

    public HeaderInterceptor setCommonHeaders(Map<String, String> headers) {
        this.headers.clear();
        if (headers != null) {
            this.headers.putAll(headers);
        }
        return this;
    }

    public HeaderInterceptor putHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HeaderInterceptor removeHeaders() {
        headers.clear();
        return this;
    }

    public HeaderInterceptor removeHeader(String key) {
        if (key != null && headers.containsKey(key)) {
            headers.remove(key);
        }
        return this;
    }

    public HeaderInterceptor setHeaderMode(boolean isAdd) {
        this.isAddMode = isAdd;
        return this;
    }
}
