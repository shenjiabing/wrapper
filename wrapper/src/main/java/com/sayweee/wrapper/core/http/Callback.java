package com.sayweee.wrapper.core.http;

import com.sayweee.wrapper.core.model.FailureVo;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:
 */
public interface Callback {

    /**
     * 分发成功的结果
     *
     * @param call
     * @param response
     */
    void dispatchSuccess(Call call, Response response);

    /**
     * 分发失败的结果
     *
     * @param code
     * @param data
     * @param call
     * @param t
     */
    void dispatchFailure(int code, FailureVo data, Call call, Throwable t);
}
