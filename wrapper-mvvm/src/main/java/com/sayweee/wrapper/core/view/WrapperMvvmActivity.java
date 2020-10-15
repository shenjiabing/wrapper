package com.sayweee.wrapper.core.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sayweee.wrapper.CoreConfig;
import com.sayweee.wrapper.base.view.WrapperDialog;
import com.sayweee.wrapper.core.BaseLoaderModel;
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
        BaseLoaderModel loaderModel = null;
        if (viewModel == null) {
            Class target = null;
            Type type = getClass().getGenericSuperclass();
            Logger.toJson(type);
            if (type instanceof ParameterizedType) {
                try {
                    Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                    Logger.toJson(types);
                    if (types != null && types.length > 0) {
                        Type temp = types[0];
                        Logger.toJson(temp);
                        if (temp instanceof Class<?>) { //当前是范型直接class，不再后续范型
                            target = (Class<?>) temp;
                            if (!BaseViewModel.class.isAssignableFrom(target)) { //二次检验
                                Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                                        + " >>> the view model not implement BaseViewModel"));
                                target = null;
                            }
                        } else {
                            Type rawType = ((ParameterizedType) temp).getRawType();
                            if (rawType instanceof Class<?> && BaseViewModel.class.isAssignableFrom((Class<?>) rawType)) {
                                //范型二次解析  目标view model
                                target = (Class) rawType;
                                //判断其下的范型
                                types = ((ParameterizedType) temp).getActualTypeArguments();
                                if (types != null && types.length > 0) {
                                    temp = types[0];
                                    Logger.toJson(type);
                                    if (temp instanceof Class<?>) {
                                        if (BaseLoaderModel.class.isAssignableFrom((Class<?>) temp)) {
                                            //范型引入的model
                                            Class model = (Class) temp;
                                            Logger.toJson(model);
                                            loaderModel = (BaseLoaderModel) model.newInstance();
                                        }
                                    } else {
                                        if (temp instanceof ParameterizedType) {
                                            //二次解析范型model
                                            Type modelType = ((ParameterizedType) temp).getRawType();
                                            if (modelType instanceof Class<?> && BaseLoaderModel.class.isAssignableFrom((Class<?>) modelType)) {
                                                Class model = (Class) modelType;
                                                Logger.toJson(model);
                                                loaderModel = (BaseLoaderModel) model.newInstance();
                                            }
                                            types = ((ParameterizedType) temp).getActualTypeArguments();
                                            if (types != null && types.length > 0) {
                                                //范型引入的service
                                                Type service = types[0];
                                                Logger.toJson(service);
                                                //为model注入service
                                                if (service instanceof Class<?>) {
                                                    loaderModel.inject((Class) service);
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
            if (target == null) {
                Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                        + " >>> not find paradigm argument for view model to bind"));
                target = BaseViewModel.class;
            }
            viewModel = (VM) ViewModelProviders.of(this).get(target);
        }
        if (loaderModel != null) {
            viewModel.injectModel(loaderModel);
        }
        //让ViewModel拥有View的生命周期感应
        viewModel.injectLifecycle(getLifecycle());
        bindLoadingStatus();
        attachModel();
    }

//    @Override
//    public void injectModel() {
//        viewModel = createModel();
//        if (viewModel == null) {
//            Class clazz = null;
//            Type type = getClass().getGenericSuperclass();
//            Logger.toJson(type);
//            if (type instanceof ParameterizedType) {
//                try {
//                    Type[] types = ((ParameterizedType) type).getActualTypeArguments();
//                    Logger.toJson(types);
//                    if (types != null && types.length > 0) {
//                        clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
//                        if (clazz != null && !BaseViewModel.class.isAssignableFrom(clazz)) {
//                            Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
//                                    + " >>> the view model not implement BaseViewModel"));
//                            clazz = null;
//                        }
//                    }
//                } catch (Exception e) {
//                    Logger.error(e);
//                }
//            }
//            if (clazz == null) {
//                Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
//                        + " >>> not find paradigm argument for view model to bind"));
//                clazz = BaseViewModel.class;
//            }
//            viewModel = (VM) ViewModelProviders.of(this).get(clazz);
//        }
//        if (viewModel != null) {
//            //让ViewModel拥有View的生命周期感应
//            viewModel.injectLifecycle(getLifecycle());
//        }
//        bindLoadingStatus();
//        attachModel();
//    }

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
