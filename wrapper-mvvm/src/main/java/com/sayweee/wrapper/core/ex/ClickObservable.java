package com.sayweee.wrapper.core.ex;

import android.os.Looper;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/25.
 * Desc:
 */
public class ClickObservable extends Observable<Object> {
    private final View view;

    private ClickObservable(View view) {
        this.view = view;
    }

    public static ClickObservable click(View view) {
        return new ClickObservable(view);
    }

    @Override
    protected void subscribeActual(Observer<? super Object> observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        if (view == null) {
            return;
        }
        ClickDisposable disposable = new ClickDisposable(view, observer);
        observer.onSubscribe(disposable);
        view.setOnClickListener(disposable);
    }

    static final class ClickDisposable extends MainThreadDisposable implements View.OnClickListener {
        private final View view;
        private final Observer<? super Object> observer;

        ClickDisposable(View view, Observer<? super Object> observer) {
            this.view = view;
            this.observer = observer;
        }

        @Override
        public void onClick(View v) {
            if (!isDisposed()) {
                observer.onNext(v);
            }
        }

        @Override
        protected void onDispose() {
            view.setOnClickListener(null);
        }
    }

    private static boolean checkMainThread(Observer<?> observer) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onError(new IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().getName()));
            return false;
        }
        return true;
    }
}
