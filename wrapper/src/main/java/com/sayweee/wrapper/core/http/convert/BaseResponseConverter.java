package com.sayweee.wrapper.core.http.convert;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/26.
 * Desc:
 */
public abstract class BaseResponseConverter<T> implements Converter<ResponseBody, T> {
    protected final Type type;
    protected final Class<?> rawType;

    protected BaseResponseConverter(Type type, Class<?> rawType) {
        this.type = type;
        this.rawType = rawType;
    }

    /**
     * 是否为空的jsonObject对象 {}
     **/
    protected boolean isEmptyJSON(@Nullable Object data) {
        return data instanceof JSONObject && ((JSONObject) data).length() == 0;
    }

    @Nullable
    protected <V> V convertBaseType(@Nullable Object data) {
        if (data == null) {
            return null;
        }
        if (data instanceof Boolean) {
            return (V) data;
        }
        if (data instanceof Number) {
            //防止JSON不是引用我们想要的类型
            Number number = (Number) data;
            //赋值时自动装箱
            Number value = null;
            if (data instanceof Integer) {
                value = number.intValue();
            } else if (data instanceof Long) {
                value = number.longValue();
            } else if (data instanceof Short) {
                value = number.shortValue();
            } else if (data instanceof Double) {
                value = number.doubleValue();
            } else if (data instanceof Float) {
                value = number.floatValue();
            } else if (data instanceof Byte) {
                value = number.byteValue();
            }
            if (value != null) {
                return (V) value;
            }
        }
        //如果是String直接返回
        if (data instanceof String) {
            return (V) String.valueOf(data);
        }
        return (V) data;
    }
}
