package com.sayweee.wrapper.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/8/22.
 * Desc:
 */
public class KeyboardUtils {

    /**
     * 展示软键盘
     * @param view
     */
    public static void showSoftInput(final View view) {
        if (view == null || view.getContext() == null) return;
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showSoftInput(imm, view);
            }
        }, 100L);
    }

    /**
     * 隐藏软键盘
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null) return;
        View view = activity.getWindow().getDecorView();
        hideSoftInput(view);
    }

    /**
     * 展示软键盘
     * @param manager
     * @param view
     */
    public static void showSoftInput(InputMethodManager manager, View view) {
        if (manager != null && view != null) {
            try {
                manager.showSoftInput(view, 0);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 隐藏软键盘
     * @param view
     */
    public static void hideSoftInput(View view) {
        if (view == null || view.getContext() == null) return;
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        hideSoftInput(imm, view);
    }

    /**
     * 隐藏软键盘
     * @param manager
     * @param view
     */
    public static void hideSoftInput(InputMethodManager manager, View view) {
        if (manager != null && view != null && view.getWindowToken() != null) {
            try {
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 设置软键盘隐藏/显示
     * @param activity
     * @param v
     * @param visible
     */
    public static void setKeyboardVisible(Activity activity, View v, boolean visible) {
        Context context = activity;
        if(context == null) {
            context = v != null ?  v.getContext() : null;
        }
        if (context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (visible) {
                    showSoftInput(imm, v);
                } else {
                    if (v == null && activity != null) {
                        v = activity.getWindow().getDecorView();
                    }
                    hideSoftInput(imm, v);
                }
            }
        }
    }
}
