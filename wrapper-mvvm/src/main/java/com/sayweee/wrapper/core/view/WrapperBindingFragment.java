package com.sayweee.wrapper.core.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.CoreConfig;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.core.model.ILoaderModel;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/14.
 * Desc:
 */
public abstract class WrapperBindingFragment<VM extends BaseViewModel<? extends ILoaderModel>, V extends ViewDataBinding> extends WrapperMvvmFragment<VM> implements IBindingView {

    protected V binding;
    private ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (useDataBinding()) {
            this.container = container;
            if (this.activity == null) {
                this.activity = getActivity();
            }
            injectBinding();
            if (binding != null) {
                return binding.getRoot();
            } else {
                Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                        + " >>> use data binding but bind failed, please check argument V or rebuild"));
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (useDataBinding() && binding != null) {
            binding.unbind();
        }
    }

    @Override
    public void injectBinding() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), getLayoutRes(), container, false);
    }

    @Override
    public boolean useDataBinding() {
        return true;
    }
}
