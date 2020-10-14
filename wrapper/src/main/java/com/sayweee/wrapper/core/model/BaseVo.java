package com.sayweee.wrapper.core.model;


import com.sayweee.logger.Logger;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:    所有继承此接口 默认不处理 直接回调回原始数据
 */
public class BaseVo implements Serializable {

    protected String _rawData;

    public void setRawData(String rawData) {
        this._rawData = rawData;
    }

    public String getRawData() {
        return _rawData;
    }

    public String getResponseMessage() {
        if (!isRawDataNull()) {
            try {
                return new JSONObject(_rawData).optString("message", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 如果返回数据是集合，数据解析方法会调用次方法获取元素的类型
     *
     * @return 参考new TypeToken<List<String>>() {}.getType()
     */
    public Type elementType() {
        return null;
    }


    /**
     * 数据解析成功后会调用此方法设置数据
     *
     * @param list
     */
    public void setDataList(List<?> list) {
        Logger.json(list);
    }

    private boolean isRawDataNull() {
        return _rawData == null || _rawData.length() == 0;
    }

}

