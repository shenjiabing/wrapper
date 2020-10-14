package com.sayweee.app2.launch;

import android.app.Application;

import androidx.annotation.NonNull;

import com.sayweee.app2.test.LoginBean;
import com.sayweee.wrapper.core.BaseLoaderModel;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.bean.ResponseBean;
import com.sayweee.wrapper.core.compat.ResponseObserver;
import com.sayweee.wrapper.core.compat.ResponseTransformer;
import com.sayweee.logger.Logger;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/13.
 * Desc:
 */
public class LaunchViewModel extends BaseViewModel<BaseLoaderModel<LaunchApi>> {

    public LaunchViewModel(@NonNull Application application) {
        super(application);
    }

    public void testVoid() {
        getLoader().getHttpService()
                .testVoid("heardown@163.com", "123456", null)
                .compose(ResponseTransformer.scheduler(this))
                .subscribe(new ResponseObserver<ResponseBean<Void>>(false) {

                    @Override
                    public void onBegin() {
                        super.onBegin();
                        setLoadingStatus(true);
                    }

                    @Override
                    public void onResponse(ResponseBean<Void> response) {
                        Logger.json(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        setLoadingStatus(true);
                    }
                });
    }

    public void testNormal() {
        getLoader().getHttpService()
                .testNormal("heardown@163.com", "123456", null)
                .compose(ResponseTransformer.scheduler(this))
                .subscribe(new ResponseObserver<ResponseBean<LoginBean>>() {
                    @Override
                    public void onResponse(ResponseBean<LoginBean> response) {
                        Logger.json(response);
                    }
                });
    }
}
