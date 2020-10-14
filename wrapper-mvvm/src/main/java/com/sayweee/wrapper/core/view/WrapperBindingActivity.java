package com.sayweee.wrapper.core.view;

import android.view.View;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.CoreConfig;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.core.R;
import com.sayweee.wrapper.core.model.ILoaderModel;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/13.
 * Desc:
 */
public abstract class WrapperBindingActivity<VM extends BaseViewModel<? extends ILoaderModel>, V extends ViewDataBinding> extends WrapperMvvmActivity<VM> implements IBindingView {

    protected V binding;

    /**
     * 重写设置布局
     * 若不使用dataBinding，请设置{@WrapperBindingActivity#useDataBinding()}返回false
     * 重写 先设置binding逻辑，再调用原model关联逻辑
     */
    @Override
    protected void setContentView() {
        if (useDataBinding()) {
            this.activity = this;
            injectBinding();
            injectModel();
        } else {
            super.setContentView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useDataBinding() && binding != null) {
            binding.unbind();
        }
    }

    @Override
    public View getContentView() {
        return useDataBinding() && binding != null ? binding.getRoot() : super.getView();
    }

    @Override
    public void injectBinding() {
        if (useWrapper()) {
            setContentView(R.layout.activity_wrapper);
            FrameLayout container = findViewById(R.id.container);
            binding = DataBindingUtil.bind(View.inflate(this, getLayoutRes(), null));
            container.addView(binding.getRoot());
        } else {
            binding = DataBindingUtil.setContentView(this, getLayoutRes());
        }
        if (binding == null) {
            Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >>> use data binding but bind failed, please check argument V or rebuild"));
        }
        if (useStatusBarWrapper()) {
            initStatusBar();
        }
    }

    @Override
    public boolean useDataBinding() {
        return true;
    }
}
