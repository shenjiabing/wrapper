package com.sayweee.wrapper.base.view;

import android.content.Context;
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
public abstract class WrapperMvpFragment<P extends BaseMvpPresenter> extends WrapperLazyFragment implements IMvpView {
    protected P presenter;
    protected WrapperDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Class<? extends MvpPresenter> presenterClass = (Class<? extends MvpPresenter>) type.getActualTypeArguments()[0];
        try {
            this.presenter = (P) presenterClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }

        presenter.attachView(this);
        progressDialog = createLoadingDialog(getActivity());
    }

    @Override
    public void onDestroy() {
        onStopLoading();
        progressDialog = null;
        super.onDestroy();
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
    public void onStopLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void beforeSuccess() {

    }

    @Override
    public boolean onResponse(Call call, Response response) {
        return false;
    }

    @Override
    public boolean onError(String url, FailureBean dataVo) {
        return false;
    }

    protected WrapperDialog createLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }

}
