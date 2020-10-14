package com.sayweee.app2.test;


import com.sayweee.wrapper.bean.ResponseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/26.
 * Desc:
 */
public interface TestApi {
    @FormUrlEncoded
    @POST("login/api_login_with_email")
    Observable<ResponseBean<Void>> testVoid(@Field("email") String email,
                                            @Field("password") String password,
                                            @Field("channelID") String channelID);

    @FormUrlEncoded
    @POST("login/api_login_with_email")
    Observable<NResponseBean> testN(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("channelID") String channelID);

    @FormUrlEncoded
    @POST("login/api_login_with_email")
    Observable<ResponseBean<LoginBean>> testNormal(@Field("email") String email,
                                                   @Field("password") String password,
                                                   @Field("channelID") String channelID);

}
