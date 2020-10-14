package com.sayweee.app2.launch;

import com.sayweee.app2.test.LoginBean;
import com.sayweee.app2.test.NResponseBean;
import com.sayweee.wrapper.bean.ResponseBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/13.
 * Desc:
 */
public interface LaunchApi {

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
