package com.sayweee.wrapper.core.http;

import android.text.TextUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/8/13.
 * Desc:
 */
public abstract class DomainInterceptor implements Interceptor {

    public final static String DOMAIN = "domain_url";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        try {
            String domainUrl = null;
            Request.Builder builder = request.newBuilder();
            List<String> headerValues = request.headers(DOMAIN);
            if (headerValues != null && headerValues.size() > 0) {
                builder.removeHeader(DOMAIN);
                domainUrl = headerValues.get(0);
            }
            if (TextUtils.isEmpty(domainUrl)) {
                domainUrl = getBaseHost();
            }
            Request build = builder.url(domainUrl).build();
            return chain.proceed(build);
        } catch (Exception e) {
            return chain.proceed(request);
        }
    }

    protected abstract String getBaseHost();
}
