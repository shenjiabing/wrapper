package com.sayweee.widget.tabbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2018/9/5.
 * Desc:
 */
public abstract class TabItem extends FrameLayout {

    public TabItem(@NonNull Context context) {
        super(context);
    }

    public TabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setTabPosition(int position);


    public abstract int getTabPosition();

    /**
     * 设置选中状态
     */
    public abstract void setSelected(boolean selected);

    /**
     * 设置消息数字。注意：数字需要大于0才会显示
     */
    public abstract void setBadgeMessage(int num);

    /**
     * 设置是否显示无数字的小圆点
     */
    public abstract void setBadgeDot(boolean visible);

    /**
     * 设置角标图片
     * @param drawable
     */
    public abstract void setBadgeExpand(Drawable drawable);
    /**
     * 隐藏红点和文字
     */
    public abstract void dismissBadge();
}

