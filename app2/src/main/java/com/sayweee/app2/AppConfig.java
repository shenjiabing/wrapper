package com.sayweee.app2;

import android.app.Application;
import android.content.Context;

import com.sayweee.wrapper.http.RetrofitIml;
import com.sayweee.wrapper.http.interceptor.LogInterceptor;
import com.sayweee.wrapper.CoreConfig;

import java.io.File;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/12.
 * Desc:
 */
public class AppConfig {
    //日志地址根据当前环境决定，/sdcard/Android/data/包名/file/log
    public static String PATH_LOG = null;

    public final static int DAY_LOG_KEEP = 3;   //日志保存时间
    public final static String PATH_LOG_NAME = "log";
    public final static String PATH_TEMP_NAME = "temp";
    public final static String PATH_HTTP_NAME = "http/interceptor"; //本地网络日志
    public final static String FLAG_LOG = "com.sayweee.www:log";
    public final static String FLAG_DEBUG = "com.sayweee.www:debug";
    public final static String FLAG_DEBUG_SLEF = "beihai308";//"1949..";

    public final static String KEY_APPS_FLYER = "MBtHQsoWwGtmCzLaxyY9sU";   //appsflyer
    public static final String API_PRODUCT = "https://www.sayweee.com";

    public final static String FLAVORS_OFFICIAL = "official";
    public final static String FLAVORS_DEV = "dev";

    //支持的第三方授权方式
    public final static String OPTION_WECHAT = "wechat";
    public final static String OPTION_FACEBOOK = "facebook";
    public final static String OPTION_KAKAO = "kakao";

    public final static String DOMAIN_HOST_PRODUCT = "https://api.sayweee.net";
    public final static String DOMAIN_HOST_TB1 = "http://api.tb1.sayweee.net";
    public final static String DOMAIN_HOST_BX1 = "http://api.bx1.sayweee.net";


    public static void attach(Application application) {
        CoreConfig.get().attach(application);
        LogConfig.setLogConfig(getStoragePath(application, AppConfig.PATH_LOG_NAME), BuildConfig.DEBUG, true, DAY_LOG_KEEP);
        RetrofitIml.get().attach(application)
                .initHttp(API_PRODUCT, null, new LogInterceptor(LogInterceptor.Level.BODY));
    }


    public static String getStoragePath(Context context, String dirName) {
        try {
            File file = new File(context.getExternalFilesDir(null), dirName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        } catch (Exception e) {
            File file = new File(context.getFilesDir(), dirName);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        }
    }

}
