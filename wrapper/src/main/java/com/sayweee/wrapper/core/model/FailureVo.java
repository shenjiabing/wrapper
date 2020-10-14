package com.sayweee.wrapper.core.model;

import android.text.TextUtils;

import com.sayweee.wrapper.core.http.convert.HttpError;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:
 */
public class FailureVo extends BaseVo {
    public int code;
    public boolean result;  //请求结果
    private String message;

    /**
     *
     * @param result
     * @param message
     * @param raw       原始数据  二次解析无法获得 使用需判空 现仅使用baseVo解析返回空时返回
     */
    public FailureVo(boolean result, String message, String raw) {
        this.result = result;
        this.message = message;
        this._rawData = raw;
    }

    public FailureVo(int code, boolean result, String message, String raw) {
        this.code = code;
        this.result = result;
        this.message = message;
        this._rawData = raw;
    }

    public boolean isResponseResultFailed() {
        return code == HttpError.CODE_RESPONSE_RESULT_FAILED;
    }

    public String getMessage() {
        String responseMessage = getResponseMessage();
        if(TextUtils.isEmpty(responseMessage)) {
            return message;
        }
        return responseMessage;
    }
}
