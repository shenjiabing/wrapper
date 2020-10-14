package com.sayweee.wrapper.core.http;

import androidx.annotation.Nullable;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:    替换修改{@link Request#url()}
 */
public abstract class ReplaceUrlCallFactory extends CallFactoryProxy {

    public ReplaceUrlCallFactory(Call.Factory delegate) {
        super(delegate);
    }

    @Override
    public final Call newCall(Request request) {
        okhttp3.HttpUrl newHttpUrl = getNewUrl(request);
        if (newHttpUrl != null) {
            Request newRequest = request.newBuilder().url(newHttpUrl).build();
            return delegate.newCall(newRequest);
        }

        return delegate.newCall(request);
    }

    /**
     * @return new httpUrl, if null use old httpUrl
     */
    @Nullable
    protected abstract HttpUrl getNewUrl(Request request);

}