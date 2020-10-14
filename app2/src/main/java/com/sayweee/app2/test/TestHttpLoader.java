package com.sayweee.app2.test;


import com.sayweee.wrapper.core.BaseLoaderModel;
import com.sayweee.wrapper.bean.ResponseBean;
import com.sayweee.wrapper.core.compat.ResponseTransformer;

import io.reactivex.Observable;


/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class TestHttpLoader extends BaseLoaderModel<TestApi> implements TestApi {

    @Override
    public Observable<ResponseBean<Void>> testVoid(String email, String password, String channelID) {
        return service.testVoid(email, password, channelID).compose(ResponseTransformer.scheduler(this));
    }

    @Override
    public Observable<NResponseBean> testN(String email, String password, String channelID) {
        return service.testN(email, password, channelID).compose(ResponseTransformer.scheduler(this));
    }

    @Override
    public Observable<ResponseBean<LoginBean>> testNormal(String email, String password, String channelID) {
        return service.testNormal(email, password, channelID).compose(ResponseTransformer.scheduler(this));
    }
}
