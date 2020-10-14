package com.sayweee.wrapper.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/25.
 * Desc:    缓存拦截器
 */
public class GzipInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request origin = chain.request();
        if (origin.body() == null || origin.header("Content-Encoding") != null) {
            return chain.proceed(origin);
        }
        Request request = origin.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(origin.method(), createGzip(origin.body()))
                .build();
        return chain.proceed(request);
    }

    private RequestBody createGzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() throws IOException {
                return super.contentLength();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };

    }
}
