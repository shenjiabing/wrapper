package com.sayweee.wrapper.base.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sayweee.wrapper.base.presenter.BaseMvpPresenter;
import com.sayweee.wrapper.base.presenter.MvpPresenter;
import com.sayweee.wrapper.core.model.FailureVo;
import com.sayweee.wrapper.widget.LoadingDialog;
import com.sayweee.wrapper.widget.Toaster;

import java.lang.reflect.ParameterizedType;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public abstract class WrapperDialogFragment<P extends BaseMvpPresenter> extends DialogFragment implements IMvpView {
    protected P presenter;
    protected WrapperDialog progressDialog;
    protected Activity activity;

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
        activity = getActivity();
        progressDialog = createLoadingDialog(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getLayoutRes(), container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); //去除标题栏
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState);
        loadData(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
        presenter.detachView();
        presenter = null;
    }


    @Override
    public void onLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
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
    public boolean onError(String url, FailureVo dataVo) {
        return false;
    }

    @Override
    public boolean onResponse(Call call, Response response) {
        return false;
    }

    protected WrapperDialog createLoadingDialog(Context context) {
        return new LoadingDialog(context);
    }


    protected void showToast(String msg) {
        Toaster.showToast(msg);
    }

    protected abstract int getLayoutRes();

    /**
     * 初始化View。或者其他view级第三方控件的初始化,及相关点击事件的绑定
     *
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 获取请求参数，初始化vo对象，并发送请求
     *
     * @param savedInstanceState
     */
    protected abstract void loadData(Bundle savedInstanceState);
}
