package com.sayweee.app2.test;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.sayweee.wrapper.core.ExceptionHandler;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class RxUtils {

    public static <T> LifecycleTransformer<T> bindToLifecycle(Context context) {
        if (context instanceof LifecycleProvider) {
            return ((LifecycleProvider) context).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("current context not the LifecycleProvider type");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull Fragment fragment) {
        if (fragment instanceof LifecycleProvider) {
            return ((LifecycleProvider) fragment).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("current fragment not the LifecycleProvider type");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(LifecycleProvider lifecycle) {
        return lifecycle.bindToLifecycle();
    }

    public static  ObservableTransformer schedulersTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static ObservableTransformer exceptionTransformer() {
        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.onErrorResumeNext(new Function<Throwable, ObservableSource>() {
                    @Override
                    public ObservableSource apply(Throwable throwable) throws Exception {
                        return Observable.error(ExceptionHandler.handleException(throwable));
                    }
                });
            }
        };
    }
}
