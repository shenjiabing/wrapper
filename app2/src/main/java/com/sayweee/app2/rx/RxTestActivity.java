package com.sayweee.app2.rx;

import com.sayweee.app2.test.LoginBean;
import com.sayweee.logger.Logger;
import com.sayweee.wrapper.bean.FailureBean;
import com.sayweee.wrapper.bean.ResponseBean;
import com.sayweee.wrapper.core.BaseLoaderModel;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.core.compat.ResponseObserver;
import com.sayweee.wrapper.core.compat.ResponseTransformer;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/15.
 * Desc:
 */
public class RxTestActivity extends PanelActivity<BaseViewModel<BaseLoaderModel<RxApi>>> {
    public final static String TAG_LOAD_NORMAL = "load_normal";
    public final static String TAG_LOAD_ERROR = "load_error";

    @Override
    protected void initView() {
        addItems(
                TAG_LOAD_NORMAL,
                TAG_LOAD_ERROR

        );
    }

    @Override
    public void onClick(String tag) {
        switch (tag) {
            case TAG_LOAD_NORMAL:
                viewModel.loader.service.testNormal("heardown@163.com", "123456", null).compose(ResponseTransformer.scheduler())
                .subscribe(new ResponseObserver<ResponseBean<LoginBean>>() {
                    @Override
                    public void onResponse(ResponseBean<LoginBean> response) {
                        Logger.toJson(response);
                    }
                });
                break;
            case TAG_LOAD_ERROR:
                viewModel.loader.service.testNormal("heardown@163.com", "123", null).compose(ResponseTransformer.scheduler())
                        .subscribe(new ResponseObserver<ResponseBean<LoginBean>>() {
                            @Override
                            public void onResponse(ResponseBean<LoginBean> response) {
                                Logger.toJson(response);
                            }

                            @Override
                            public void onError(FailureBean failure) {
                                super.onError(failure);
                                Logger.toJson(failure);
                            }
                        });
                break;
        }
    }


}
