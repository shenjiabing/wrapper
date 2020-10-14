package com.sayweee.wrapper.core.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sayweee.wrapper.CoreConfig;
import com.sayweee.wrapper.base.view.WrapperDialog;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.core.model.ILoaderModel;
import com.sayweee.logger.Logger;
import com.sayweee.wrapper.base.view.WrapperActivity;
import com.sayweee.wrapper.widget.LoadingDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/30.
 * Desc:    mvvm封装
 */
public abstract class WrapperMvvmActivity<VM extends BaseViewModel<? extends ILoaderModel>> extends WrapperActivity implements IMvvmView, ILoadingView {

    protected VM viewModel;
    protected WrapperDialog loadingDialog;

    @Override
    protected void setContentView() {
        super.setContentView();
        injectModel();
    }

    @Override
    public <M> M createModel() {
        return null;
    }

    @Override
    public void injectModel() {
        viewModel = createModel();
        if (viewModel == null) {
            Class clazz = null;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                try {
                    Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                    if (types != null && types.length > 0) {
                        clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
                        if (clazz != null && !BaseViewModel.class.isAssignableFrom(clazz)) {
                            Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                                    + " >>> the view model not implement BaseViewModel"));
                            clazz = null;
                        }
                    }
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
            if (clazz == null) {
                Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                        + " >>> not find paradigm argument for view model to bind"));
                clazz = BaseViewModel.class;
            }
            viewModel = (VM) ViewModelProviders.of(this).get(clazz);
        }
        if (viewModel != null) {
            //让ViewModel拥有View的生命周期感应
            viewModel.injectLifecycle(getLifecycle());
        }
        bindLoadingStatus();
        attachModel();
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
        }
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected void bindLoadingStatus() {
        if (viewModel != null) {
            viewModel.getLoadingStatus().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean flag) {
                    if (flag) {
                        showLoading();
                    } else {
                        hideLoading();
                    }
                }
            });
        }
    }
}
