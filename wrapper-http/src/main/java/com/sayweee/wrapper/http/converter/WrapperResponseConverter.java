package com.sayweee.wrapper.http.converter;

import com.google.gson.Gson;
import com.sayweee.wrapper.http.ResponseException;
import com.sayweee.wrapper.bean.BaseBean;

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
public class WrapperResponseConverter<T> implements Converter<ResponseBody, T> {

    public final static String resultName = "result";
    public final static String messageName = "message";
    public final static String dataName = "object";

    protected final Gson gson;
    protected final Type type;
    protected final Class<?> rawType;

    protected WrapperResponseConverter(Gson gson, Type type, Class<?> rawType) {
        this.type = type;
        this.rawType = rawType;
        this.gson = gson;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        try {
            JSONObject jsonObject = new JSONObject(json);
            final boolean result = jsonObject.getBoolean(resultName);
            final String message = jsonObject.getString(messageName);
            if (!result) { //
                throw new ResponseException(ResponseException.ERROR_RESPONSE_STATUS_FAILED, message, json);
            }
            //当前类为BaseBean的情况，不校验data是否为null
            if (BaseBean.class == rawType) {
                BaseBean bean = new BaseBean();
                return (T) bean;
            }
            //防止服务端忽略data字段导致jsonObject.get("data")方法奔溃
            //没返回data
            if (jsonObject.isNull(dataName)) {
                throw new ResponseException(ResponseException.ERROR_RESPONSE_DATA_NULL, "data is null", json);
            }
            //data里内容为空
            Object data = jsonObject.get(dataName);
            if (isEmptyJSON(data)) {
                throw new ResponseException(ResponseException.ERROR_RESPONSE_DATA_CONTENT_NULL, "data content is null", json);
            }
            String dataStr = data.toString();
            if (dataStr.startsWith("[")) { //返回的data类型是集合的情况
                BaseBean vo = (BaseBean) rawType.newInstance();
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
            throw new ResponseException(ResponseException.ERROR_PARSE, "data error", json);
        } catch (Exception e) {
            throw new ResponseException(ResponseException.ERROR_PARSE, e.getMessage(), json);
        } finally {
            value.close();
        }
    }

    /**
     * 是否为空的jsonObject对象 {}
     **/
    protected boolean isEmptyJSON(Object data) {
        return data instanceof JSONObject && ((JSONObject) data).length() == 0;
    }

}
