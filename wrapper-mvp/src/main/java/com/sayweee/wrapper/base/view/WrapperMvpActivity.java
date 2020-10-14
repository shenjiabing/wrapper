package com.sayweee.wrapper.base.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.base.presenter.MvpPresenter;
import com.sayweee.wrapper.bean.FailureBean;
import com.sayweee.wrapper.widget.LoadingDialog;

import java.lang.reflect.ParameterizedType;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public abstract class WrapperMvpActivity<P extends BaseMvpPresenter> extends WrapperActivity implements IMvpView {
    protected P presenter;
    protected WrapperDialog progressDialog;

    @Override
    protected void setContentView() {
        super.setContentView();
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<? extends MvpPresenter> presenterClass = (Class<? extends MvpPresenter>) type.getActualTypeArguments()[0];
        try {
            this.presenter = (P) presenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        presenter.attachView(this);
        progressDialog = createLoadingDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStopLoading();
        progressDialog = null;
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void onLoading() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void beforeSuccess() {

    }

    @Override
    public void onStopLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onError(String url, FailureBean dataVo) {
        return false;
    }

    @Override
    public boolean onResponse(Call call, Response response) {
        return false;
    }

    protected WrapperDialog createLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

    /**
     * 从intent中获取请求参数，初始化vo对象，并发送请求
     *
     * @param savedInstanceState
     * @param intent
     */
    protected abstract void loadData(Bundle savedInstanceState, Intent intent);

}

