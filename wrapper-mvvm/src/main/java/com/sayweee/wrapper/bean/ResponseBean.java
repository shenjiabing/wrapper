package com.sayweee.wrapper.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/26.
 * Desc:    范型 T 即是data解析的对象
 *          若结果不需要对data里内容进行判断，范型T可用Void/Object代替或者不填
 *          建议配合{@com.sayweee.core.compat.ResponseObserver}使用
 *          注意此类默认严格模式，会对data内容进行判断，若data = null，将会走onError事件
 *
 *          例如 "{\"result\":true,\"message\":"request success"}" 这种缺少data的结构的判断
 *          需要针对设置{@ResponseObserver#useStrictMode} = false，
 *          关闭严格模式，此种情况下将不对data进行校验
 *
 */
public class ResponseBean<T> {
    public boolean result;
    public String message;
    @SerializedName(value = "object", alternate = "data")
    public T object;


    //使用fast json时忽略输出
    @JSONField(serialize = false)
    public boolean isSuccess() {
        return result;
    }

    @JSONField(serialize = false)
    public T getData() {
        return object;
    }
}
