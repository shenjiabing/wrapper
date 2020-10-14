package com.sayweee.widget.nest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent3;
import androidx.viewpager.widget.ViewPager;

import com.sayweee.widget.R;

import java.lang.reflect.Field;


/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/6/5.
 * Desc:
 */
public class ParentRecyclerView extends BaseNestRecyclerView implements NestedScrollingParent3 {
    final static int TAG_KEY = 1001001;

    private View childPagerContainer = null;
    private ViewPager innerViewPager = null;
    private boolean doNotInterceptTouchEvent = false;
    private boolean innerIsStickyTop = false;
    private int stickyHeight = 0; //顶部悬停的高度
    private StickyListener stickyListener;

    public interface StickyListener {
        void onSticky(boolean isAtTop);
    }


    public ParentRecyclerView(@NonNull Context context) {
        super(context);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ParentRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (e != null && e.getAction() == MotionEvent.ACTION_DOWN) {
            ChildRecyclerView childRecyclerView = findCurrentChildRecyclerView();
            // 1. 是否禁止拦截
            doNotInterceptTouchEvent = doNotInterceptTouch(e.getRawY(), childRecyclerView);
            // 2. 停止Fling
            this.stopFling();
            if (childRecyclerView != null) {
                childRecyclerView.stopFling();
            }
        }
        if (doNotInterceptTouchEvent) {
            return false;
        }
        return super.onInterceptTouchEvent(e);
    }

    /**
     * 是否禁止拦截TouchEvent事件
     */
    private boolean doNotInterceptTouch(float rawY, ChildRecyclerView childRecyclerView) {
        if (childRecyclerView != null && childPagerContainer != null) {
            int[] size = new int[2];
            childRecyclerView.getLocationOnScreen(size);

            int childRecyclerViewY = size[1];
            if (rawY > childRecyclerViewY) {
                return true;
            }
            if (childPagerContainer.getTop() == stickyHeight) {
                return true;
            }
        }
        // 默认不禁止
        return false;
    }


    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        // do nothing
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        // do nothing
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        // do nothing
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        // do nothing
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof ChildRecyclerView) {
            // 下面这一坨代码的主要目的是计算consumeY
            int consumeY = dy;
            int childScrollY = ((ChildRecyclerView) target).computeVerticalScrollOffset();
            if (childPagerContainer != null) {
                if (childPagerContainer.getTop() > stickyHeight) {
                    if (childScrollY > 0 && dy < 0) {
                        consumeY = 0;
                    } else if (childPagerContainer.getTop() - dy < stickyHeight) {
                        consumeY = childPagerContainer.getTop() - stickyHeight;
                    }
                } else if (childPagerContainer.getTop() == stickyHeight) {
                    consumeY = -dy < childScrollY ? 0 : dy + childScrollY;
                }
            }
            if (consumeY != 0) {
                consumed[1] = consumeY;
                this.scrollBy(0, consumeY);
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        if (state == SCROLL_STATE_IDLE) {
            int velocityY = getVelocityY();
            if (velocityY > 0) {
                // 滑动到最底部时，骤然停止，这时需要把速率传递给ChildRecyclerView
                ChildRecyclerView childRecyclerView = findCurrentChildRecyclerView();
                if (childRecyclerView != null) {
                    childRecyclerView.fling(0, velocityY);
                }
            }
        }
    }

    /**
     * 获取当前的ChildRecyclerView
     *
     * @return
     */
    private ChildRecyclerView findCurrentChildRecyclerView() {
        if (innerViewPager != null) {
            int currentItem = innerViewPager.getCurrentItem();
            int childCount = innerViewPager.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View itemChildView = innerViewPager.getChildAt(i);
                if (itemChildView != null) {
                    ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) itemChildView.getLayoutParams();
                    try {
                        Field positionField = layoutParams.getClass().getDeclaredField("position");
                        positionField.setAccessible(true);
                        int position = (int) positionField.get(layoutParams);
                        if (!layoutParams.isDecor && currentItem == position) {
                            if (itemChildView instanceof ChildRecyclerView) {
                                return (ChildRecyclerView) itemChildView;
                            } else {
                                Object tagView = itemChildView.getTag(R.id.tag_saved_child_recycler_view);
                                if (tagView != null && tagView instanceof ChildRecyclerView) {
                                    return (ChildRecyclerView) tagView;
                                }
                            }
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public void setInnerViewPager(ViewPager viewPager) {
        this.innerViewPager = viewPager;
    }

    /**
     * 由ChildRecyclerView上报ViewPager(ViewPager2)的父容器，用做内联滑动逻辑判断，及Touch拦截等
     *
     * @param childPagerContainer
     */
    public void setChildPagerContainer(View childPagerContainer) {
        if (this.childPagerContainer != childPagerContainer) {
            this.childPagerContainer = childPagerContainer;
            this.post(new Runnable() {
                @Override
                public void run() {
                    adjustChildPagerContainerHeight();
                }
            });
        }
    }

    /**
     * Activity调用方法
     *
     * @param stickyHeight
     */
    public void setStickyHeight(int stickyHeight) {
        final int scrollOffset = this.stickyHeight - stickyHeight;
        this.stickyHeight = stickyHeight;
        this.adjustChildPagerContainerHeight();
        this.post(new Runnable() {
            @Override
            public void run() {
                scrollBy(0, scrollOffset);
            }
        });
    }

    /**
     * 调整Child容器的高度
     */
    private void adjustChildPagerContainerHeight() {
        if (childPagerContainer != null) {
            ViewGroup.LayoutParams params = childPagerContainer.getLayoutParams();
            if (params != null) {
                int height = this.getHeight() - stickyHeight;
                if (height != params.height) {
                    params.height = height;
                    childPagerContainer.setLayoutParams(params);
                }
            }
        }
    }

    /**
     * 设置吸顶回调
     *
     * @param listener
     */
    public void setStickyListener(StickyListener listener) {
        this.stickyListener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        boolean currentStickyTop = false;
        if (childPagerContainer != null && childPagerContainer.getTop() == stickyHeight) {
            currentStickyTop = true;
        }
        if (currentStickyTop != innerIsStickyTop) {
            innerIsStickyTop = currentStickyTop;
            if (stickyListener != null) {
                stickyListener.onSticky(innerIsStickyTop);
            }
        }
    }
}
