package com.sayweee.wrapper;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.sayweee.scheduler.SafeDispatchHandler;
import com.sayweee.wrapper.utils.CrashHandler;
import com.sayweee.wrapper.widget.Toaster;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/30.
 * Desc:
 */
public class CoreConfig {

    protected Application application;
    protected Handler mainHandler;
    protected boolean tipsEnable;

    private static class Builder {
        private static CoreConfig instance = new CoreConfig();
    }

    public static CoreConfig get() {
        return Builder.instance;
    }

    public CoreConfig attach(Application application) {
        this.application = application;
        onAttach();
        return this;
    }

    protected void onAttach() {
        CrashHandler.install();
        Toaster.attach(application);
    }

    public Handler getMainThreadHandler() {
        if(mainHandler == null) {
            mainHandler = new SafeDispatchHandler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    public Application getApplication() {
        return application;
    }

    public CoreConfig setTipsEnable(boolean tipsEnable) {
        this.tipsEnable = tipsEnable;
        return this;
    }

    public boolean isTipsEnable(){
        return tipsEnable;
    }
}
