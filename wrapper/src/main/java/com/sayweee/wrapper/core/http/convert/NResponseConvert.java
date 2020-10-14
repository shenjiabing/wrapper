package com.sayweee.wrapper.core.http.convert;

import com.sayweee.wrapper.core.model.N;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/26.
 * Desc:    继承N的解析，传递默认的raw数据
 */
public class NResponseConvert<T> implements Converter<ResponseBody, T> {
    private final Class<?> clazz;

    public NResponseConvert(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convert(ResponseBody body) throws IOException {
        String value = body.string();
        try {
            T t = (T) clazz.newInstance();
            ((N) t).setRawData(value);
            return t;
        } catch (IllegalAccessException | InstantiationException | ClassCastException e) {
            throw new HttpError(HttpError.CODE_PARSE_ERROR, e.getMessage(), value);
        } finally {
            body.close();
        }

    }
}
