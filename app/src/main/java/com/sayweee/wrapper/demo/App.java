package com.sayweee.wrapper.demo;

import android.app.Application;

import com.sayweee.wrapper.ManagerConfig;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/21.
 * Desc:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ManagerConfig.getInstance().attach(this)
                .initHttpClient("http://tb1.sayweee.net")
                .createService(HttpApi.class);

    }
}
