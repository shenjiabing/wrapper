package com.sayweee.wrapper.base.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.StyleRes;

import com.sayweee.wrapper.R;
import com.sayweee.wrapper.listener.OnViewHelper;

/**
 * Author:  winds
 * Data:    2017/10/17
 * Version: 1.0
 * Desc:
 */
public abstract class WrapperDialog implements OnViewHelper {

    protected Dialog dialog;
    protected Context context;
    protected ViewHelper helper;

    public WrapperDialog(Context context) {
        this(context, R.style.CommonDialogTheme);
    }

    public WrapperDialog(Context context, @StyleRes int themeResId) {
        this.context = context;
        this.dialog = new Dialog(context, themeResId);
        dialog.setContentView(getHelperView(null, getLayoutRes(), this));
        setDialogParams(dialog);
    }

    @Override
    public abstract void help(ViewHelper helper);

    /**
     * 实例化对应layoutId的view同时生成ViewHelper
     *
     * @param group    可为null
     * @param layoutId
     * @param listener
     * @return
     */
    protected View getHelperView(ViewGroup group, int layoutId, OnViewHelper listener) {
        helper = new ViewHelper(LayoutInflater.from(context).inflate(layoutId, group, false));
        if (listener != null) {
            listener.help(helper);
        }
        return helper.getItemView();
    }

    public WrapperDialog show() {
        if (dialog != null && !isShowing()) {
            try{
                dialog.show();
            }catch (Exception e){}
        }
        return this;
    }

    public WrapperDialog dismiss() {
        if (isShowing()) {
            try {
                dialog.dismiss();
            }catch (Exception e){}
        }
        return this;
    }

    public boolean isShowing() {
        if (dialog != null && dialog.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 设置绝对参数的参考实现
     *
     * @param dialog
     * @param width
     * @param height
     * @param gravity
     */
    protected void setDialogAbsParams(Dialog dialog, int width, int height, int gravity) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = dp2px(width);
        params.height = dp2px(height);
        window.setGravity(gravity);
        window.setAttributes(params);
    }

    /**
     * 设置参数的参考实现
     *
     * @param dialog
     * @param width
     * @param height
     * @param gravity
     */
    protected void setDialogParams(Dialog dialog, int width, int height, int gravity) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        window.setGravity(gravity);
        window.setAttributes(params);
    }

    /**
     * 设置参数的参考实现
     *
     * @param gravity
     */
    public void setDialogParams(Dialog dialog, int gravity) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(gravity);
        window.setAttributes(params);
    }

    public WrapperDialog addHelperCallback(HelperCallback callback) {
        callback.help(dialog, helper);
        return this;
    }

    public WrapperDialog addHelperAbsCallback(HelperAbsCallback callback) {
        callback.help(this, dialog, helper);
        return this;
    }

    protected abstract int getLayoutRes();

    /**
     * 设置dialog的参数
     *
     * @param dialog
     */
    protected abstract void setDialogParams(Dialog dialog);

    public interface HelperCallback {
        /**
         * 默认帮助方式
         *
         * @param dialog 用来设置dialog的一些默认参数
         * @param helper 用于布局修改与实现
         */
        void help(Dialog dialog, ViewHelper helper);
    }

    public interface HelperAbsCallback {
        void help(WrapperDialog wrapper, Dialog dialog, ViewHelper helper);
    }

    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
    }
}
