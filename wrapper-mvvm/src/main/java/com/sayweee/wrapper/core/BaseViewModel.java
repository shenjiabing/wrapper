package com.sayweee.wrapper.core;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.sayweee.wrapper.CoreConfig;
import com.sayweee.wrapper.core.compat.SingleLiveEvent;
import com.sayweee.wrapper.core.model.BaseModel;
import com.sayweee.wrapper.core.lifecycle.IViewModel;
import com.sayweee.wrapper.core.model.IModel;
import com.sayweee.logger.Logger;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class BaseViewModel<M extends BaseModel> extends AndroidViewModel implements IViewModel, Consumer<Disposable> {

    public M loader;

    protected int count;

    protected boolean forceDisableShow;

    protected WeakReference<Lifecycle> lifecycle;

    protected List<IModel> models = new ArrayList<>();

    protected SingleLiveEvent<Boolean> loadingStatus = new SingleLiveEvent<>();

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }

    @Override
    public void injectLifecycle(Lifecycle provider) {
        this.lifecycle = new WeakReference<>(provider);
        provider.addObserver(this);
        onModelAttach(loader == null ? injectModel() : loader);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (loader != null) {
            loader.onDetach();
        }
        if (models != null && models.size() > 0) {
            for (int i = 0; i < models.size(); i++) {
                IModel model = models.get(i);
                if (model != null) {
                    model.onDetach();
                }
            }
            models.clear();
        }
        if (loadingStatus != null) {
            loadingStatus.postValue(false);
        }
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        if (loader != null && loader instanceof BaseLoaderModel) {
            ((BaseLoaderModel) loader).accept(disposable);
        } else {
            Logger.error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >> not find default model! lack of disposable processor"));
        }
    }

    /**
     * 自动关联model
     *
     * @return
     */
    private M injectModel() {
        Class clazz = null;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] types = parameterizedType.getActualTypeArguments();
                if (types != null && types.length > 0) {
                    Type target = parameterizedType.getActualTypeArguments()[0];

                    //范型解析 BaseViewModel<T>
                    if (target instanceof Class && (BaseLoaderModel.class.isAssignableFrom((Class<?>) target))) {
                        clazz = (Class<?>) target;
                    }
                    if (clazz != null) {
                        return (M) clazz.newInstance();
                    }

                    //范型解析 BaseViewModel<T<S>>
                    if (target instanceof ParameterizedType) {
                        parameterizedType = (ParameterizedType) target;
                        target = parameterizedType.getRawType();

                        if (target instanceof Class && (BaseLoaderModel.class.isAssignableFrom((Class<?>) target))) {
                            clazz = (Class<?>) target;
                            M m = (M) clazz.newInstance();

                            Type[] arguments = parameterizedType.getActualTypeArguments();
                            Type service = null;
                            if (arguments != null && arguments.length > 0) {
                                service = arguments[0];
                            }
                            if (service != null) {
                                clazz = Class.forName(service.getTypeName());
                                if (clazz != null) {
                                    ((BaseLoaderModel) m).inject(clazz);
                                }
                            } else {
                                Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                                        + " >> no service inject to model! please check model argument S"));
                            }
                            return m;

                        }
                    }
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                + " >> model inject failed!"));
        return null;
    }

    private void onModelAttach(M m) {
        this.loader = m;
        if (loader != null) {
            loader.onAttach();
        } else {
            Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >> no model bind with view model! please check view model or not use"));
        }
    }

    /***对外暴露API***********************************************************************************/

    /**
     * loading的观察者对象
     *
     * @return
     */
    public SingleLiveEvent<Boolean> getLoadingStatus() {
        return loadingStatus;
    }

    /**
     * 设置默认的model
     *
     * @param loader
     */
    public void setModel(M loader) {
        onModelAttach(loader);
    }

    /**
     * 获取自动关联的model
     *
     * @return
     */
    public M getLoader() {
        return loader;
    }

    /**
     * 注入自己的model
     *
     * @param model
     */
    public void addModel(IModel model) {
        if (model != null) {
            model.onAttach();
            models.add(model);
        }
    }

    /**
     * 移除注入的model
     *
     * @param model
     */
    public void removeModel(IModel model) {
        if (model != null) {
            model.onDetach();
            if (models.contains(model)) {
                models.remove(model);
            }
        }
    }

    /**
     * 设置loading状态
     *
     * @param isAdd
     */
    public void setLoadingStatus(boolean isAdd) {
        count = isAdd ? count + 1 : count - 1;
        count = Math.max(count, 0);
        if (!forceDisableShow) {
            loadingStatus.postValue(count > 0);
        }
    }

    /**
     * 强制关闭弹窗
     *
     * @param forceDisableShow
     */
    public void setForceDisableShow(boolean forceDisableShow) {
        this.forceDisableShow = forceDisableShow;
        loadingStatus.setValue(false);
    }

}
