package com.sayweee.wrapper.core.model;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/6/9.
 * Desc:
 */
public interface FailureCode {
    int FAILURE_CONVERT = -101;   //请求转换出错
    int FAILURE_NETWORK = -201;   //请求出错
    int FAILURE_SERVER = -301;    //服务器问题
    int FAILURE_RETROFIT = -401;  //Rotrofit onFailure
    int FAILURE_RESPONSE = -501;  //response 返回result = false
}
