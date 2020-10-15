package com.sayweee.app2.test;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayweee.wrapper.bean.FailureBean;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.bean.ResponseBean;
import com.sayweee.wrapper.core.compat.ResponseObserver;
import com.sayweee.wrapper.core.compat.ResponseTransformer;
import com.sayweee.logger.Logger;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/27.
 * Desc:
 */
public class TestViewModel extends BaseViewModel<TestHttpLoader> {

    public ObservableField<Boolean> observableField = new ObservableField<>();

    ObservableInt observableInt = new ObservableInt();

    public TestViewModel(@NonNull Application application) {
        super(application);
    }

    public void testN() {
        loader.testN("heardown@163.com", "123456", null)
                .subscribe(new ResponseObserver<NResponseBean>() {

                    @Override
                    public void onBegin() {
                        super.onBegin();
                        setLoadingStatus(true);
                    }

                    @Override
                    public void onResponse(NResponseBean response) {
                        Logger.json(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        setLoadingStatus(false);
                    }
                });
    }

    public void testVoid() {
        String s = "{\"result\":true,\"success\":true}";
        String s1= "{\"object\":{},\"result\":true,\"success\":true}";
        String s2= "{\"object\":{\"result\":true},\"result\":true,\"success\":true}";

        Observable.just(s)
                .map(new Function<String, ResponseBean<Void>>() {
                    @Override
                    public ResponseBean<Void> apply(String s) throws Exception {
                        Type type = new TypeToken<ResponseBean<Void>>() {}.getType();
//                        ResponseBean<Void> object = (ResponseBean<Void>) JSON.parseObject(s, type);
                        ResponseBean<Void> voidResponseBean = new Gson().fromJson(s, type);
                        return voidResponseBean;
                    }
                })
        .compose(ResponseTransformer.scheduler())
        .subscribe(new ResponseObserver<ResponseBean<Void>>(false) {
            @Override
            public void onResponse(ResponseBean<Void> response) {
                Logger.json(response);
            }

            @Override
            public void onError(FailureBean failure) {
                super.onError(failure);
                Logger.json(failure);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

//        loader.testVoid("heardown@163.com", "123456", null)
//                .subscribe(new ResponseObserver2<ResponseBean<Void>>() {
//                    @Override
//                    public void onBegin() {
//                        super.onBegin();
//                        loadingStatus.setValue(true);
//                    }
//
//                    @Override
//                    public void onResponse(ResponseBean<Void> response) {
//                        Logger.json(response);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        loadingStatus.setValue(false);
//                    }
//                });
    }

    public void testNormal() {
        loader.testNormal("heardown@163.com", "123456", null)
//                .subscribe(new Observer<ResponseBean<LoginBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        loadingStatus.setValue(true);
//                    }
//
//                    @Override
//                    public void onNext(ResponseBean<LoginBean> response) {
//                        Logger.json(response);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        loadingStatus.setValue(false);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        loadingStatus.setValue(false);
//                    }
//                });
                .subscribe(new ResponseObserver<ResponseBean<LoginBean>>() {

                    @Override
                    public void onBegin() {
                        super.onBegin();
                        setLoadingStatus(true);
                    }

                    @Override
                    public void onResponse(ResponseBean<LoginBean> response) {
                        Logger.json(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        setLoadingStatus(false);
                    }
                });
    }

}
