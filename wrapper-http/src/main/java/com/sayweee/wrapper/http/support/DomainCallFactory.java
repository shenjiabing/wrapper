package com.sayweee.wrapper.http.support;

import com.sayweee.logger.Logger;

import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:    替换修改{@link Request#url()}
 */
public class DomainCallFactory extends CallFactoryProxy {
    public final static String KEY_DOMAIN = "domain_url";
    //此字段用于强制指定domain的url不被替换，需配合KEY_DOMAIN字段一起用于添加到headers中
    //此字段不校验为何值，只检验有无
    public final static String KEY_DOMAIN_FORCE = "domain_force";
    private String baseUrl;

    private String targetHost;
    private boolean autoConvert = false;

    public DomainCallFactory(Call.Factory delegate) {
        super(delegate);
    }

    public DomainCallFactory(Call.Factory delegate, String baseUrl) {
        super(delegate);
        this.baseUrl = baseUrl;
    }

    @Override
    public final Call newCall(Request request) {
        List<String> headers = request.headers(KEY_DOMAIN);
        if (headers != null && headers.size() > 0) {
            String newUrl = headers.get(0);
            if (newUrl != null) {
                try {
                    HttpUrl httpUrl = request.url();
                    String path = request.url().toString();
                    String host = (httpUrl.isHttps() ? "https://" : "http://") + request.url().host();
                    String url = null;
                    List<String> forceHeader = request.headers(KEY_DOMAIN_FORCE);
                    if (forceHeader != null && forceHeader.size() > 0) {
                        url = path.replace(host, newUrl);
                    } else {
                        url = path.replace(host, autoConvert ? targetHost : newUrl);
                    }
                    Request.Builder builder = request.newBuilder();
                    builder.removeHeader(KEY_DOMAIN);
                    Request newRequest = builder.url(HttpUrl.parse(url)).build();
                    return delegate.newCall(newRequest);
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
        }
        return delegate.newCall(request);
    }

    /**
     * 替换domain中设置的host
     * @param targetHost
     */
    public void targetHost(String targetHost) {
        this.autoConvert = true;
        this.targetHost = targetHost;
    }

    /**
     * 用于设置是否转换domain中的host 优先级低于KEY_DOMAIN_FORCE
     * @param autoConvert
     */
    public void setAutoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
    }
}