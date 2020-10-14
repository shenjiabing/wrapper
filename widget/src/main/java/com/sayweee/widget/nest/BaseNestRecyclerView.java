package com.sayweee.widget.nest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/6/5.
 * Desc:
 */
public class BaseNestRecyclerView extends RecyclerView {

    protected OverScroller overScroller;

    protected Object scrollerYObj;

    protected Field velocityYField;

    public BaseNestRecyclerView(@NonNull Context context) {
        super(context);
    }

    public BaseNestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseNestRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        try {
            // 1. mViewFlinger对象获取
            Field mViewFlingerField = getClass().getDeclaredField("mViewFlinger");
            mViewFlingerField.setAccessible(true);
            Object mViewFlingObj = mViewFlingerField.get(this);

            // 2. overScroller对象获取
            Field mScrollerYFiled = mViewFlingObj.getClass().getDeclaredField("mOverScroller");
            mScrollerYFiled.setAccessible(true);
            overScroller = (OverScroller) mScrollerYFiled.get(mViewFlingObj);

            // 3. scrollerY对象获取
            Field mScrollerYField = OverScroller.class.getDeclaredField("mScrollerY");
            mScrollerYField.setAccessible(true);
            scrollerYObj = mScrollerYField.get(overScroller);

            // 4. Y轴速率filed获取
            velocityYField = scrollerYObj.getClass().getDeclaredField("mCurrVelocity");
            velocityYField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取垂直方向的速率
     *
     * @return
     */
    public int getVelocityY() {
        try {
            return (int) velocityYField.get(scrollerYObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 停止滑动fling
     */
    public void stopFling() {
        try {
            this.overScroller.forceFinished(true);
            velocityYField.set(scrollerYObj, 0);
        } catch (Exception e) {
            e.printStackTrace();
            stopScroll();
        }
    }

}
