package com.sayweee.widget.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Author:  winds
 * Data:    2018/1/15
 * Version: 1.0
 * Desc:
 */
public class UltraViewPager extends ViewPager {

    private UltraViewPager.ScrollMode scrollMode = UltraViewPager.ScrollMode.HORIZONTAL;
    private boolean endPageDisscroll = true;   //最后的子页面是否禁止滑动  默认禁止

    public enum ScrollMode {
        HORIZONTAL(0), VERTICAL(1);
        int id;

        ScrollMode(int id) {
            this.id = id;
        }

        static ScrollMode getScrollMode(int id) {
            for (ScrollMode scrollMode : values()) {
                if (scrollMode.id == id)
                    return scrollMode;
            }
            throw new IllegalArgumentException();
        }
    }

    public UltraViewPager(Context context) {
        this(context, null);
    }

    public UltraViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClipChildren(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setScrollMode(UltraViewPager.ScrollMode scrollMode) {
        this.scrollMode = scrollMode;
        if (scrollMode == UltraViewPager.ScrollMode.VERTICAL)
            setPageTransformer(false, new UltraVerticalTransformer());
    }

    /**
     * 最后的子页面是否禁止滑动
     *
     * @param endPageDisscroll 默认true 禁止滑动
     */
    public void setEndPageDisscroll(boolean endPageDisscroll) {
        this.endPageDisscroll = endPageDisscroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollMode == ScrollMode.VERTICAL) {
            return super.onTouchEvent(swapTouchEvent(ev));
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollMode == UltraViewPager.ScrollMode.VERTICAL) {
            boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(ev));
            //If not intercept, touch event should not be swapped.
            swapTouchEvent(ev);
            return intercept;
        }
        return super.onInterceptTouchEvent(ev);
    }

    int startX;
    int startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getRawX();
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getRawX();
                int endY = (int) ev.getRawY();

                if (Math.abs(endX - startX) < Math.abs(endY - startY)) {// 上下滑动
                    if (endPageDisscroll && getCurrentItem() == getAdapter().getCount() - 1) {
                        if (getChildAt(getAdapter().getCount() - 1) != null) {
                            getChildAt(getAdapter().getCount() - 1).dispatchTouchEvent(ev);
                        }
                        return true;
                    }
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 交换事件
     * @param event
     * @return
     */
    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }
}
