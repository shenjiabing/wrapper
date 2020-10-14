package com.sayweee.wrapper.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/27.
 * Desc:
 */
public class Toaster {

    static Toast sToast;
    static Toast sViewToast;

    public static void attach(Context context) {
        if (context != null) {
            sToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
        }
    }

    public static void showToast(CharSequence msg) {
        if (sToast != null) {
            sToast.setText(msg);
            show();
        }
    }

    public static void showToast(CharSequence msg, boolean longShow) {
        if (sToast != null) {
            sToast.setText(msg);
            sToast.setDuration(longShow ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            show();
        }
    }

    public static void showToast(@StringRes int textRes) {
        if (sToast != null) {
            Context context = sToast.getView().getContext();
            if (context != null) {
                showToast(context.getString(textRes));
            }
        }
    }

    public static void showToast(View view) {
        showToast(view, false);
    }

    public static void showToast(View view, boolean longShow) {
        if (sToast != null && view != null) {
            if (sViewToast == null) {
                Context context = sToast.getView().getContext();
                if (context != null) {
                    sViewToast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
                }
            }
            if (sViewToast != null) {
                sViewToast.setView(view);
                sViewToast.setDuration(longShow ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                show(sViewToast);
            }
        }
    }

    private static void show() {
        show(sToast);
    }

    private static void show(Toast toast) {
        if (toast != null) {
            try {
                toast.show();
            } catch (Exception e) {
            }
        }
    }
}
