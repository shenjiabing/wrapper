package com.sayweee.widget.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * 可控制是否允许滑动的ViewPager
 * 默认false
 */
public class ScrollableViewPager extends ViewPager {
    private boolean scrollable = false;

    public ViewPager setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        return null;
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollableViewPager(Context context) {
        super(context);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!scrollable)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!scrollable)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

}