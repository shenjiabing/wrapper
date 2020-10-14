package com.sayweee.wrapper.demo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;


/**
 * Created by ycy on 2016/11/16.
 * 对应Http接口的Java Interface类
 */

public interface HttpApi {
    /*POST 有参数请求：
        1、可以用@Field进行参数设置，可以有多个
        2、可以用@FieldMap进行参数设置用Map集合进行参数
        */

    //email注册请求
    @FormUrlEncoded
    @POST("login/api_app_sign_up")
    Call<String> getEmailRegisterInfo(@Field("email") String email,
                                      @Field("password") String password,
                                      @Field("channelID") String channelID);

    //email登陆请求
    @FormUrlEncoded
    @POST("login/api_login_with_email")
    Call<LoginInfo> getEmailLoginInfo(@Field("email") String email,
                                      @Field("password") String password,
                                      @Field("channelID") String channelID);

    //韩国kakao登陆请求
    @FormUrlEncoded
    @POST("login/api_login_with_kakao_access_token")
    Call<LoginInfo> getKakaoLoginInfo(@Field("access_token") String access_token,
                                      @Field("channelID") String channelID);


    //facebook第三方登陆请求
    @FormUrlEncoded
    @POST("login/api_login_with_fb_user_info")
    Call<LoginInfo> getFbLoginInfo(@Field("user_info") String info,
                                   @Field("refresh_token") String token,
                                   @Field("channelID") String channelID);

    //微信第三方登陆请求by code
    @FormUrlEncoded
    @POST("login/api_login_with_wx_app_code")
    Call<LoginInfo> getWxLoginInfo(@Field("code") String code,
                                   @Field("channelID") String channelID);

    /**
     * 下载文件
     * 如果下载大文件的一定要加上  @Streaming  注解
     *
     * @param fileUrl 文件的路径
     * @return 请求call
     */
    @GET
    Call<ResponseBody> download(@Url String fileUrl);


}
