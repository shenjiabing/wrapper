package com.sayweee.app2.launch;

import android.os.Bundle;
import android.view.View;

import com.sayweee.app2.R;
import com.sayweee.app2.test.NResponseBean;
import com.sayweee.wrapper.core.compat.ResponseObserver;
import com.sayweee.wrapper.core.compat.ResponseTransformer;
import com.sayweee.wrapper.core.view.WrapperMvvmActivity;
import com.sayweee.logger.Logger;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/13.
 * Desc:
 */
public class LaunchActivity extends WrapperMvvmActivity<LaunchViewModel> implements View.OnClickListener {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_launch;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.btn_test_n).setOnClickListener(this);
        findViewById(R.id.btn_test_normal).setOnClickListener(this);
        findViewById(R.id.btn_test_void).setOnClickListener(this);
    }

    @Override
    public void attachModel() {

    }

    @Override
    public void loadData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_test_n:
                viewModel.loader.getHttpService().testN("heardown@163.com", "123456", null)
                        .compose(ResponseTransformer.scheduler())
                        .subscribe(new ResponseObserver<NResponseBean>() {
                            @Override
                            public void onResponse(NResponseBean response) {
                                Logger.json(response);
                            }
                        });
                break;
            case R.id.btn_test_normal:
                viewModel.testNormal();
                break;
            case R.id.btn_test_void:
                viewModel.testVoid();
                break;
        }
    }
}
