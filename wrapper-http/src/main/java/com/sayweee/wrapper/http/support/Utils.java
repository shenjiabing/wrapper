package com.sayweee.wrapper.http.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/10.
 * Desc:
 */
public class Utils {

    /**
     * 判断当前是否有网络
     * 默认true
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            try {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission")
                NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
                return info != null && info.isConnected();
            } catch (Exception e) {

            }
        }
        return true;
    }
}
