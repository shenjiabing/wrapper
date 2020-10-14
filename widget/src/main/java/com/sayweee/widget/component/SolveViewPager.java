package com.sayweee.widget.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Author:  winds
 * Data:    2017/4/27
 * Desc:    可上下左右滑动控制的ViewPager
 */

public class SolveViewPager extends ViewPager {
    int startX;
    int startY;
    boolean updowneable = false;   //上下滑动事件是否需要父控件拦截    默认不需要 false
    boolean leftable = true;       //向左滑动事件  默认需要true
    boolean rightable = true;      //向右滑动事件  默认需要true

    public SolveViewPager(Context context) {
        super(context);
    }

    public SolveViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件分发, 请求父控件是否拦截事件
     * 1. 右划, 而且是第一个页面, 需要父控件拦截
     * 2. 左划, 而且是最后一个页面, 需要父控件拦截
     * 3. 上下滑动, 不需要父控件拦截
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);// 不要拦截,
                // 这样是为了保证ACTION_MOVE调用
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动
                    if (endX > startX) {// 右划
                        if (getCurrentItem() == 0) {// 第一个页面, 需要父控件拦截
                            getParent().requestDisallowInterceptTouchEvent(!rightable);
                        }
                    } else {// 左划
                        if (getCurrentItem() == getAdapter().getCount() - 1) {// 最后一个页面,
                            // 需要拦截
                            getParent().requestDisallowInterceptTouchEvent(!leftable);
                        }
                    }
                } else {// 上下滑动
                    getParent().requestDisallowInterceptTouchEvent(!updowneable); //不需要父控件拦截
                }

                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    public void setLeftable(boolean leftable) {
        this.leftable = leftable;
    }
    public void setRightable(boolean rightable) {
        this.rightable = rightable;
    }
    public void setUpdowneable(boolean updowneable) {
        this.updowneable = updowneable;
    }
}
