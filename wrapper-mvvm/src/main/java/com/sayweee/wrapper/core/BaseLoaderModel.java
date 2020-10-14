package com.sayweee.wrapper.core;

import com.sayweee.wrapper.CoreConfig;
import com.sayweee.wrapper.core.model.BaseModel;
import com.sayweee.wrapper.core.model.ILoaderModel;
import com.sayweee.wrapper.http.RetrofitIml;
import com.sayweee.logger.Logger;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:    Model层，用于数据请求分发
 */
public class BaseLoaderModel<S> extends BaseModel implements ILoaderModel<S>, Consumer<Disposable> {

    protected S service;
    protected CompositeDisposable disposables;

    @Override
    public S inject() {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            try {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                if (types != null && types.length > 0) {
                    Class clazz = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                    if (clazz != null) {
                        return (S) createHttpService(clazz);
                    }
                }
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        return null;
    }

    @Override
    public void inject(Class<?> clazz) {
        this.service = (S) createHttpService(clazz);
    }

    @Override
    public S getHttpService() {
        return service;
    }

    @Override
    public <T> T createHttpService(Class<T> clazz) {
        return RetrofitIml.get().getHttpService(clazz);
    }

    @Override
    public void onAttach() {
        if (service == null) {
            service = inject();
        }
        if (service == null) {
            Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >>> not find default service, please check argument S or not use"));
        }
        disposables = new CompositeDisposable();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearDisposable();
    }

    @Override
    public void accept(Disposable disposable) throws Exception {
        if (disposables != null) {
            if (disposable != null) {
                disposables.add(disposable);
            }
        } else {
            Logger.enable(CoreConfig.get().isTipsEnable()).error(new IllegalArgumentException(getClass().getSimpleName()
                    + " >>> lack of default disposables, maybe cause memory leak"));
        }
    }

    protected void clearDisposable() {
        if (disposables != null) {
            disposables.clear();
        }
    }

}
