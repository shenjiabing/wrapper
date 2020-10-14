package com.sayweee.wrapper.base.view;

import com.sayweee.wrapper.core.model.BaseVo;
import com.sayweee.wrapper.core.model.FailureVo;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public interface IMvpView {

    /**
     * 开始加载数据时回调此方法，用以显示加载ProgressDialog或者其他的的操作
     */
    void onLoading();

    /**
     * 加载数据完成回调方法
     */
    void onStopLoading();

    /**
     * 数据加载成功后的回调，在真正处理前回调
     */
    void beforeSuccess();

    /**
     * 默认请求数据解析成功后，将数据填充到View，并显示View
     *
     * @param url    请求的url
     * @param dataVo 解析成功后返回VO对象
     */
    void onResponse(String url, BaseVo dataVo);

    /**
     * 加载失败回调
     *
     * @param url    请求的url，用于判断当前请求
     * @param dataVo 解析的对象
     * @return true 不会执行默认处理方法 false会回调默认处理方法
     */
    boolean onError(String url, FailureVo dataVo);

    /**
     * 请求结果回调 提前于onResponse(String url, BaseVo dataVo)
     *
     * @param call     请求的call对象
     * @param response 请求返回的response对象
     * @return true 不会回调onResponse(String url, BaseVo dataVo)  false 若该回调为正常回调会重新回调onResponse(String url, BaseVo dataVo)
     */
    boolean onResponse(Call call, Response response);
}
