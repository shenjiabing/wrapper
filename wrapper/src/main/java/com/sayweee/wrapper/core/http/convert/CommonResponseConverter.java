package com.sayweee.wrapper.core.http.convert;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.sayweee.wrapper.core.model.BaseVo;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/25.
 * Desc:
 */
public final class CommonResponseConverter<T> implements Converter<ResponseBody, T> {
    public final static String resultName = "result";
    public final static String messageName = "message";
    public final static String dataName = "object";

    protected final Gson gson;
    protected final Type type;
    protected final Class<?> rawType;

    protected CommonResponseConverter(Gson gson, Type type, Class<?> rawType) {
        this.type = type;
        this.rawType = rawType;
        this.gson = gson;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        String cacheStr = value.string();
        try {
            JSONObject jsonObject = new JSONObject(cacheStr);
            final boolean result = jsonObject.getBoolean(resultName);
            final String message = jsonObject.getString(messageName);
            if (!result) {
                throw new HttpError(HttpError.CODE_RESPONSE_RESULT_FAILED, message, cacheStr);
            }
            if (BaseVo.class == rawType) {
                BaseVo baseVo = new BaseVo();
                baseVo.setRawData(cacheStr);
                return (T) baseVo;
            }
            //防止服务端忽略data字段导致jsonObject.get("data")方法奔溃
            //且能判断为null或JSONObject#NULL的情况
            if (jsonObject.isNull(dataName)) {
                throw new HttpError(HttpError.CODE_RESPONSE_DATA_ERROR, "data is null", cacheStr);
            }
            Object data = jsonObject.get(dataName);
            if (isEmptyJSON(data)) {
                throw new HttpError(HttpError.CODE_RESPONSE_DATA_ERROR, "data is null", cacheStr);
            }
            String dataStr = data.toString();
            if (dataStr.startsWith("[")) { //返回的data类型是集合的情况
                BaseVo vo = (BaseVo) rawType.newInstance();
                Type type = vo.elementType();
                if (type != null) {
                    Object list = gson.fromJson(dataStr, type);
                    if (list instanceof List) {
                        vo.setDataList((List<?>) list);
                    }
                    return (T) vo;
                }
            }
            T t = gson.fromJson(dataStr, type);
            if (t != null) {
                //防止线上接口修改导致反序列化失败奔溃
                return t;
            }
            throw new HttpError(HttpError.CODE_RESPONSE_DATA_ERROR, "data error", cacheStr);
        } catch (Exception e) {
            throw new HttpError(HttpError.CODE_PARSE_ERROR, "data parse error", cacheStr);
        } finally {
            value.close();
        }
    }

    /**
     * 是否为空的jsonObject对象 {}
     **/
    protected boolean isEmptyJSON(@Nullable Object data) {
        return data instanceof JSONObject && ((JSONObject) data).length() == 0;
    }


}
