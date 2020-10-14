package com.sayweee.wrapper.http.support;

import android.os.Build;
import android.util.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sayweee.wrapper.http.RetrofitIml;
import com.sayweee.logger.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/10.
 * Desc:
 */
public class RequestParams {

    Map<String, Serializable> params;

    public RequestParams() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.params = new ArrayMap<>();
        } else {
            this.params = new HashMap<>();
        }
    }

    public RequestParams put(String key, Serializable value) {
        if (key != null) {
            params.put(key, value);
        }
        return this;
    }

    /**
     * 去除null 空字符串 -1 加入集合
     *
     * @param key
     * @param value
     * @return
     */
    public RequestParams putNonNull(String key, Serializable value) {
        if (key != null && value != null) {
            if (value instanceof String) {
                if ((((String) value).trim().length() == 0)) {
                    return this;
                }
            }
            if (value instanceof Integer) {
                if (((Integer) value) == -1) {
                    return this;
                }
            }
            put(key, value);
        }
        return this;
    }

    public Serializable get(String key) {
        return params.get(key);
    }

    public Map<String, Serializable> get() {
        Logger.enable(RetrofitIml.get().isLogEnable()).json(params);
        return params;
    }

    public RequestBody create() {
        String data = JSON.toJSONString(params);
        return create(data, okhttp3.MediaType.parse("application/json; charset=utf-8"));
    }

    public RequestBody create(MediaType mediaType) {
        String data = JSON.toJSONString(params);
        return create(data, mediaType);
    }

    public RequestBody create(String data, MediaType mediaType) {
        Logger.enable(RetrofitIml.get().isLogEnable()).json(data);
        return RequestBody.create(mediaType, data);
    }

    public JSONObject convert(Map<String, Serializable> params) {
        JSONObject obj = new JSONObject();
        if (params != null && params.size() > 0) {
            Iterator<Map.Entry<String, Serializable>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Serializable> entry = iterator.next();
                obj.put(entry.getKey(), entry.getValue());
            }
        }
        return obj;
    }

    public RequestBody convert(JSONObject obj, MediaType mediaType) {
        String data = JSON.toJSONString(obj);
        return create(data, mediaType);
    }

    public RequestBody convert(Map<String, Serializable> params, MediaType mediaType) {
        String data = JSON.toJSONString(params);
        return create(data, mediaType);
    }

}
