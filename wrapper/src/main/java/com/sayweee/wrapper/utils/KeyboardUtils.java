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

    public static void showSoftInput(final View view) {
        if (view == null || view.getContext() == null) return;
        final InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, 200L);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null) return;
        View view = activity.getWindow().getDecorView();
        hideSoftInput(view);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(View view) {
        if (view == null || view.getContext() == null) return;
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void setKeyboardVisible(Activity activity, View v, boolean visible) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (visible) {
                    if (v != null) {
                        imm.showSoftInput(v, 0);
                    }
                } else {
                    if (v == null) {
                        v = activity.getWindow().getDecorView();
                    }
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
    }
}
