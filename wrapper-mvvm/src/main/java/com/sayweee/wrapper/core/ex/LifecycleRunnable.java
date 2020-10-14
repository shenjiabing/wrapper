package com.sayweee.wrapper.core.ex;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/26.
 * Desc:
 */
public class LifecycleRunnable implements LifecycleObserver {

    Disposable disposable;

    public <T> void run(LifecycleOwner lifecycleOwner, final OnRunCallback<T> callback) {
        lifecycleOwner.getLifecycle().addObserver(this);
        Observable<T> observable = Observable.create(new ObservableOnSubscribe<T>() {

            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    T t = callback.onBackground();
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        callback.onStart();
                    }

                    @Override
                    public void onNext(T t) {
                        callback.onSuccess(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
