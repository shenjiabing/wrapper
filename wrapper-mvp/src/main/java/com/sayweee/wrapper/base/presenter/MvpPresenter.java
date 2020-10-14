package com.sayweee.wrapper.base.presenter;


import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import com.sayweee.wrapper.base.view.IMvpView;

import java.lang.ref.WeakReference;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public abstract class MvpPresenter<V extends IMvpView> implements Presenter<V> {

    protected WeakReference<V> viewReference; //MvpView的子类的弱引用
    protected String viewClassName; //类名 Tag

    @Override
    public void attachView(V view) {
        viewClassName = view.getClass().getSimpleName();
        viewReference = new WeakReference<>(view);
    }

    @Override
    public boolean isAttached() {
        return viewReference != null && viewReference.get() != null;
    }

    @Override
    public void detachView() {
        if (viewReference != null) {
            viewReference.clear();
            viewReference = null;
        }
    }

    /**
     * 获取实现MvpView接口的Activity或者Fragment的引用用来实现回调
     *
     * @return
     */
    public V getView() {
        return viewReference == null ? null : viewReference.get();
    }

    /**
     * 获取当前绑定对象的context对象
     *
     * @return
     */
    public Context getContext() {
        V v = getView();
        if (v != null) {
            if (v instanceof Activity) {
                return (Activity) v;
            } else if (v instanceof Fragment) {
                return ((Fragment) v).getContext();
            }
        }
        return null;
    }

}
