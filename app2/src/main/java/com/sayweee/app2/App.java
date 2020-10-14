package com.sayweee.app2;

import android.app.Application;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/28.
 * Desc:
 */
public class App extends Application {
    Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        AppConfig.attach(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //语言配置
    }
}
