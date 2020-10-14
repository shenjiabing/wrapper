package com.sayweee.wrapper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.base.view.WrapperMvpActivity;
import com.sayweee.wrapper.core.model.BaseVo;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/28.
 * Desc:
 */
public class HomeActivity extends WrapperMvpActivity<BaseMvpPresenter> {


    FrameLayout flContent;

    @Override
    protected boolean useWrapper() {
        return false;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    protected void loadData(Bundle savedInstanceState, Intent intent) {

    }

    @Override
    public void onResponse(String url, BaseVo dataVo) {

    }

}
