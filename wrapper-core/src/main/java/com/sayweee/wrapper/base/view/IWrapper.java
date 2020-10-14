package com.sayweee.wrapper.base.view;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.StringRes;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/28.
 * Desc:
 */
public interface  IWrapper {

    /**
     * 是否使用wrapper包装
     * 若为false，和wrapper相关的功能将无法使用
     * @return
     */
    boolean useWrapper();

    /**
     * 获取标题栏
     * @return
     */
    View getWrapperTitle();

    /**
     * 设置标题
     * @param title
     */
    void setWrapperTitle(CharSequence title);

    /**
     * 设置标题
     * @param titleRes
     */
    void setWrapperTitle(@StringRes int titleRes);

    /**
     * 设置返回点击事件
     */
    View.OnClickListener getOnBackListener();

    /**
     * 设置内容区域和标题栏分隔线
     * @param divider
     */
    void setWrapperDivider(Drawable divider);

    /**
     * 是否开启键盘的自动模式
     * @return
     */
    boolean autoKeyboardEnable();
}
