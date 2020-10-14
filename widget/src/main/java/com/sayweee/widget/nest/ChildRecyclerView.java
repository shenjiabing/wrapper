package com.sayweee.widget.nest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.sayweee.widget.R;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/6/5.
 * Desc:
 */
public class ChildRecyclerView extends BaseNestRecyclerView {

    public final static int DRAG_IDLE = 0;
    public final static int DRAG_VERTICAL = 1;
    public final static int DRAG_HORIZONTAL = 2;

    protected ParentRecyclerView parentRecyclerView;
    protected int mTouchSlop;
    protected float downX;
    protected float downY;
    protected int dragState = DRAG_IDLE;


    public ChildRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        //是否已经停止scrolling
        if (state == SCROLL_STATE_IDLE) {
            // 这里是考虑到当整个childRecyclerView被detach之后，及时上报parentRecyclerView
            int velocityY = getVelocityY();
            if (velocityY < 0 && computeVerticalScrollOffset() == 0) {
                if (parentRecyclerView != null) {
                    parentRecyclerView.fling(0, velocityY);
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            // ACTION_DOWN 触摸按下，保存临时变量
            dragState = DRAG_IDLE;
            downX = e.getRawX();
            downY = e.getRawY();
            this.stopFling();
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            // ACTION_MOVE 判定垂直还是水平滑动
            if (dragState == DRAG_IDLE) {
                float xDistance = Math.abs(e.getRawX() - downX);
                float yDistance = Math.abs(e.getRawY() - downY);

                if (xDistance > yDistance && xDistance > mTouchSlop) {
                    // 水平滑动
                    dragState = DRAG_HORIZONTAL;
                } else if (yDistance > xDistance && yDistance > mTouchSlop) {
                    // 垂直滑动
                    dragState = DRAG_VERTICAL;
                    //上下滑动时取消左右滑动的事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
        }
        return super.onTouchEvent(e);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        connectToParent();
    }

    /**
     * 跟ParentView建立连接，主要两件事情 -
     * 1. 将自己上报ViewPager/ViewPager2，通过tag关联到currentItem的View中
     * 2. 将ViewPager/ViewPager2报告给ParentRecyclerView
     * 这一坨代码需要跟ParentRecyclerView连起来看，否则可能会懵
     */
    protected void connectToParent() {
        ViewPager viewPager = null;
        View lastTraverseView = this;
        View parentView = (View) getParent();
        while (parentView != null) {
            if (parentView instanceof ViewPager) {
                // 使用ViewPager，parentView顺序如下：
                // ChildRecyclerView -> 若干View -> ViewPager -> 若干View -> ParentRecyclerView
                // 此处将ChildRecyclerView保存到ViewPager最直接的子View中
                if (lastTraverseView != this) {
                    lastTraverseView.setTag(R.id.tag_saved_child_recycler_view, this);
                }
                // 碰到ViewPager，需要上报给ParentRecyclerView
                viewPager = (ViewPager) parentView;
            } else if (parentView instanceof ParentRecyclerView) {
                ((ParentRecyclerView) parentView).setInnerViewPager(viewPager);
                ((ParentRecyclerView) parentView).setChildPagerContainer(lastTraverseView);
                this.parentRecyclerView = (ParentRecyclerView) parentView;
                return;
            }
            lastTraverseView = parentView;
            parentView = (View) parentView.getParent();
        }
    }

}
