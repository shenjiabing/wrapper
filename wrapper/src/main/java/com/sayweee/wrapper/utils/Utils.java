package com.sayweee.wrapper.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.sayweee.wrapper.ManagerConfig;
import com.sayweee.wrapper.core.http.convert.CommonResponseConverter;
import com.sayweee.wrapper.core.http.convert.HttpError;
import com.sayweee.wrapper.core.model.BaseVo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/26.
 * Desc:
 */
public class Utils {
    /**
     * 检查网络是否已经连接
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ManagerConfig.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isConnected();
    }

    public static <T> T parse(Gson gson, Class<?> rawType, String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            final boolean result = jsonObject.getBoolean(CommonResponseConverter.resultName);
            final String message = jsonObject.getString(CommonResponseConverter.messageName);
            if (!result) {
                throw new HttpError(HttpError.CODE_RESPONSE_RESULT_FAILED, message, json);
            }
            if (BaseVo.class == rawType) {
                BaseVo baseVo = new BaseVo();
                baseVo.setRawData(json);
                return (T) baseVo;
            }
            //防止服务端忽略data字段导致jsonObject.get("data")方法奔溃
            //且能判断为null或JSONObject#NULL的情况
            if (jsonObject.isNull(CommonResponseConverter.dataName)) {
                throw new HttpError(HttpError.CODE_RESPONSE_DATA_ERROR, "data is null", json);
            }
            Object data = jsonObject.get(CommonResponseConverter.dataName);
            if (isEmptyJSON(data)) {
                throw new HttpError(HttpError.CODE_RESPONSE_DATA_ERROR, "data is null", json);
            }
            String dataStr = data.toString();
            if (dataStr.startsWith("[")) { //返回的data类型是集合的情况
                BaseVo vo = (BaseVo) rawType.newInstance();
                Type t = vo.elementType();
                if (t != null) {
                    Object list = gson.fromJson(dataStr, t);
                    if (list instanceof List) {
                        vo.setDataList((List<?>) list);
                    }
                    return (T) vo;
                }
            }
            T t = (T) gson.fromJson(dataStr, rawType);
            if (t != null) {
                //防止线上接口修改导致反序列化失败奔溃
                return t;
            }
            throw new HttpError(HttpError.CODE_RESPONSE_DATA_ERROR, "data error", json);
        } catch (JSONException | IllegalAccessException | InstantiationException e) {
            throw new HttpError(HttpError.CODE_PARSE_ERROR, "data parse error", json);
        }
    }

    protected static boolean isEmptyJSON(@Nullable Object data) {
        return data instanceof JSONObject && ((JSONObject) data).length() == 0;
    }

    @Nullable
    protected static <V> V convertBaseType(@Nullable Object data) {
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

    public static String readTextFromAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
