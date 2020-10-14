package com.sayweee.wrapper.core.compat;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/26.
 * Desc:
 */
public class ResponseTransformer<T> implements ObservableTransformer<T, T> {

    private boolean flag;
    private Consumer<? super Disposable> consumer;

    public static <T> ResponseTransformer<T> scheduler() {
        return scheduler(null);
    }

    public static <T> ResponseTransformer<T> scheduler(Consumer<? super Disposable> consumer) {
        return new ResponseTransformer<T>(consumer, true);
    }

    public ResponseTransformer() {
        this(true);
    }

    public ResponseTransformer(boolean observeOnMainThread) {
        this(null, observeOnMainThread);
    }

    public ResponseTransformer(Consumer<? super Disposable> consumer, boolean observeOnMainThread) {
        this.flag = observeOnMainThread;
        this.consumer = consumer;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        if (consumer != null) {
            upstream = upstream.doOnSubscribe(consumer);
        }
        return upstream
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(flag ? AndroidSchedulers.mainThread() : Schedulers.io());
    }
}
