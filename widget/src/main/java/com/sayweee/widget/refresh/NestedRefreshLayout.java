package com.sayweee.widget.refresh;

import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.OverScroller;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

import com.sayweee.widget.R;

/**
 * Author by Winds on 2016/12/15
 * Email heardown@163.com
 */
public class NestedRefreshLayout extends ViewGroup
        implements NestedScrollingParent, NestedScrollingChild {

    protected static final String TAG = NestedRefreshLayout.class.getSimpleName();
    protected static final int INVALID_POINTER = -1;
    protected int mActivePointerId = INVALID_POINTER;

    //手指是否处于屏幕滑动状态
    protected boolean mIsBeingDragged;
    //减速插值因子
    protected static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    //回滚动画时间
    protected int animDuration;

    protected int[] mScrollConsumed = new int[2];
    protected int[] mScrollOffset = new int[2];
    protected NestedScrollingChildHelper childHelper;
    protected NestedScrollingParentHelper parentHelper;

    protected int mTouchSlop;
    protected int mLastMotionY;
    protected int mNestedYOffset;

    protected View nestedTarget;
    protected View pullView;
    protected PullViewHelper pullHelper;
    protected IPullRefreshView pullRefreshView;
    protected OnRefreshListener mOnRefreshListener;

    protected Interpolator mInterpolator;
    protected PullAnimation animation;
    protected IPullRefreshView.State pullState = IPullRefreshView.State.GONE;
    protected OverScroller mScroller;

    public NestedRefreshLayout(Context context) {
        this(context, null);
    }

    public NestedRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NestedRefreshLayout);

        int pullViewHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullViewHeight, 0);
        int pullMinViewHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullMinViewHeight, 0);
        int pullMaxHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_pullMaxHeight, pullViewHeight);
        int refreshHeight = ta.getDimensionPixelSize(R.styleable.NestedRefreshLayout_refreshHeight, pullViewHeight);
        animDuration = ta.getInteger(R.styleable.NestedRefreshLayout_animPlayDuration, 300);

        final int pullviewId = ta.getResourceId(R.styleable.NestedRefreshLayout_pullView, -1);

        ta.recycle();

        if (pullviewId != -1) {
            pullView = LayoutInflater.from(context).inflate(pullviewId, this, false);
            pullViewHeight = pullView.getLayoutParams().height;
            refreshHeight = pullViewHeight;
            pullMaxHeight = pullViewHeight * 3 / 2;
        }

        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        parentHelper = new NestedScrollingParentHelper(this);
        childHelper = new NestedScrollingChildHelper(this);
        pullHelper = new PullViewHelper(pullViewHeight, pullMaxHeight, pullMinViewHeight, refreshHeight);

        setWillNotDraw(false);
        setNestedScrollingEnabled(true);

        animation = new PullAnimation() {
            @Override
            public void applyTransformationTop(int targetTop) {
                final int oldScroll = pullHelper.getScroll();
                pullHelper.setScroll(targetTop);
                final int offsetY = targetTop - oldScroll;
                if (offsetY != 0)
                    scrollTargetOffset(offsetY);
            }
        };

        addOrUpdatePullView(pullViewHeight, pullView != null ? pullView : new DefaultRefreshView(context));
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (nestedTarget != null) {
            nestedTarget.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY));
        }

        if (pullView != null) {
            pullView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(pullView.getLayoutParams().height, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        checkNestedTarget();

        if (nestedTarget == null) return;

        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int childTop = getPaddingTop();
        final int childLeft = getPaddingLeft();
        final int childWidth = width - getPaddingLeft() - getPaddingRight();
        final int childHeight = height - getPaddingTop() - getPaddingBottom();

        nestedTarget.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);

        int offsetTop = -pullView.getMeasuredHeight();

        pullView.layout(childLeft, offsetTop, childLeft + childWidth, offsetTop + pullView.getMeasuredHeight());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        checkNestedTarget();
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        parentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {

        final boolean canScrollUp = pullHelper.canScrollUp();

        if (dy > 0 && canScrollUp) {
            consumed[1] = scrollMoveOffset(dy);
        }

        final int[] parentConsumed = new int[2];
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed) {
        final int myConsumed = scrollMoveOffset(dyUnconsumed);
        final int myUnconsumed = dyUnconsumed - myConsumed;
        dispatchNestedScroll(dxConsumed, myConsumed, dxUnconsumed, myUnconsumed, null);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return flingWithNestedDispatch((int) velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (!consumed) {
            return flingWithNestedDispatch((int) velocityY);
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return parentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        parentHelper.onStopNestedScroll(target);
        stopNestedScroll();
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        childHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return childHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return childHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        childHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return childHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return childHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return childHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (!isEnabled()) return false;

        final int action = MotionEventCompat.getActionMasked(ev);

        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = (int) ev.getY();
                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int y = (int) MotionEventCompat.getY(ev, pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);

                if (yDiff > mTouchSlop && (getNestedScrollAxes() & ViewCompat.SCROLL_AXIS_VERTICAL) == 0) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                /* Release the drag */
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                stopNestedScroll();
                break;
            default:
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = MotionEventCompat.getActionMasked(ev);
        if (actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_CANCEL) {
            checkSpringBack();
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        MotionEvent vtev = MotionEvent.obtain(ev);

        final int actionMasked = MotionEventCompat.getActionMasked(ev);

        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mIsBeingDragged = false;
                mLastMotionY = (int) ev.getY();
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = MotionEventCompat.findPointerIndex(ev,
                        mActivePointerId);
                if (activePointerIndex == -1) {
                    break;
                }

                final int y = (int) MotionEventCompat.getY(ev, activePointerIndex);
                int deltaY = mLastMotionY - y;

                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
                    deltaY -= mScrollConsumed[1];
                    vtev.offsetLocation(0, mScrollOffset[1]);
                    mNestedYOffset += mScrollOffset[1];
                }

                if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                }

                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionY = y - mScrollOffset[1];
                    final int oldY = getScrollY();
                    scrollMoveOffset(deltaY);
                    final int scrolledDeltaY = getScrollY() - oldY;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    if (dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset)) {
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedYOffset += mScrollOffset[1];
                    }
                }
                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionY = (int) MotionEventCompat.getY(ev, index);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionY = (int) MotionEventCompat.getY(ev,
                        MotionEventCompat.findPointerIndex(ev, mActivePointerId));
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                stopNestedScroll();
            }
            default:
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            final int delay = y - oldY;
            if (oldX != x || oldY != y) {
                scrollMoveOffset(delay);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }

    protected void addOrUpdatePullView(int pullViewHeight, View pullView) {

        if (pullView == null || this.pullView == pullView) return;

        if (this.pullView != null && this.pullView.getParent() != null) {
            ((ViewGroup) this.pullView.getParent()).removeView(this.pullView);
        }

        this.pullView = pullView;

        if (pullView instanceof IPullRefreshView) {
            pullRefreshView = (IPullRefreshView) pullView;
        } else {
            throw new ClassCastException("the refreshView must implement the interface IPullRefreshView");
        }

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, pullViewHeight);
        addView(this.pullView, 0, layoutParams);
    }

    /**
     * 视图内容滚动至指定位置并更新下拉视图状态
     * @param deltaY
     * @return
     */
    protected int scrollMoveOffset(int deltaY) {

        if (deltaY == 0 || pullState == IPullRefreshView.State.MOVE_SRPINGBACK) {
            return deltaY;
        }

        final int oldScrollY = pullHelper.getScroll();
        final int consumed = pullHelper.checkUpdateScroll(deltaY);
        final int scrollY = pullHelper.getScroll();
        final int delta = scrollY - oldScrollY;

        if (pullState == IPullRefreshView.State.MOVE_REFRESH) {
            //待定处理
        } else {
            if (pullState != IPullRefreshView.State.MOVE_REFRESH) {
                if (pullHelper.canTouchUpToRefresh()) {
                    if (pullState != IPullRefreshView.State.MOVE_WAIT_REFRESH) {
                        pullState = IPullRefreshView.State.MOVE_WAIT_REFRESH;
                        pullRefreshView.onPullFreeHand();
                    }
                } else {
                    if (pullState != IPullRefreshView.State.MOVE_PULL) {
                        pullState = IPullRefreshView.State.MOVE_PULL;
                        pullRefreshView.onPullDowning();
                    }
                }
            }
        }


        if (delta != 0)
            scrollTargetOffset(delta);

        return consumed;

    }

    /**
     * 视图内容滚动到指定的位置
     * @param offsetY
     */
    protected void scrollTargetOffset(int offsetY) {
        pullView.bringToFront();
        scrollBy(0, offsetY);
        pullRefreshView.onPullProgress(pullHelper.getScroll(), pullHelper.getScrollPercent());
    }

    /**
     * 手离开屏幕后检查和更新状态
     */
    protected synchronized void checkSpringBack() {
        if (pullState == IPullRefreshView.State.MOVE_REFRESH) {
            if (pullHelper.canTouchUpToRefresh())
                animToRefreshPosition(pullHelper.getScroll(), null, 0);
        } else {
            if (pullState != IPullRefreshView.State.MOVE_SRPINGBACK) {
                forceRefreshToState(pullHelper.canTouchUpToRefresh());
            }
        }
    }

    /**
     * 根据手指快速滑动时候的速率滚动视图
     * @param velocityY
     * @return
     */
    protected boolean flingWithNestedDispatch(int velocityY) {
        final boolean canFilngUp = pullHelper.canScrollUp() && velocityY > 0;
//        final boolean canFilngDown = pullHelper.canScrollDown() && velocityY < 0 && (!ViewCompat.canScrollVertically(nestedTarget, -1));
//        final boolean canFling = canFilngUp || canFilngDown;
        final boolean canFling = canFilngUp;
        if (!dispatchNestedPreFling(0, velocityY)) {
            dispatchNestedFling(0, velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
        return canFling;
    }

    /**
     * 根据速率调整视图的滚动
     *
     * @param velocityY Y轴方向上的速率. 负值标识用户向下的快速滑动
     */
    public void fling(int velocityY) {
        mScroller.abortAnimation();
        mScroller.fling(0, pullHelper.getScroll(), 0, velocityY, 0, 0,
                pullHelper.getMinScroll(), pullHelper.getMaxScroll(),
                0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * 多个手指触摸屏幕状态的变更
     * @param ev
     */
    protected void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >>
                MotionEventCompat.ACTION_POINTER_INDEX_SHIFT;
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            // Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = (int) MotionEventCompat.getY(ev, newPointerIndex);
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    protected void checkNestedTarget() {
        if (!isTargetValid()) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(pullView)) {
                    nestedTarget = child;
                    break;
                }
            }
        }
    }

    protected boolean isTargetValid() {
        for (int i = 0; i < getChildCount(); i++) {
            if (nestedTarget == getChildAt(i)) {
                return true;
            }
        }
        return false;
    }

    protected void animToStartPosition(int from, Animation.AnimationListener listener, long delay) {
        animToPosition(from, 0, delay, listener);
    }

    protected void animToRefreshPosition(int from, Animation.AnimationListener listener, long delay) {
        animToPosition(from, -pullHelper.getPullRefreshHeight(), delay, listener);
    }

    protected void animToPosition(int from, int to, long delayTime, Animation.AnimationListener listener) {
        animation.reset();
        animation.setFrom(from);
        animation.setTo(to);
        animation.setStartOffset(delayTime);
        animation.setDuration(animDuration);
        animation.setInterpolator(mInterpolator);
        animation.setAnimationListener(listener);
        clearAnimation();
        startAnimation(animation);
    }

    /**
     * 更新视图至指定刷新状态
     * @param refresh
     */
    public void forceRefreshToState(boolean refresh) {
        forceRefreshToState(refresh, 0);
    }

    /**
     * 更新视图至指定刷新状态
     * @param refresh
     * @param delay
     */
    public void forceRefreshToState(boolean refresh, long delay) {
        final int scrollY = pullHelper.getScroll();
        if (refresh) {
            pullState = IPullRefreshView.State.MOVE_REFRESH;
            animToRefreshPosition(scrollY, refreshingListener, 0);
        } else {
            postDelayed(delayAnimToStartPosTask, delay);
        }
    }

    /**
     * 更新视图至指定刷新状态
     */
    public void startRefresh() {
        forceRefreshToState(true);
    }

    /**
     * 【刷新完成】后执行【回滚隐藏动画】
     */
    public void refreshFinish() {
        refreshDelayFinish(500);
    }

    /**
     * 【刷新完成】后延迟指定时间执行【回滚隐藏动画】
     * @param delayTime
     */
    public void refreshDelayFinish(int delayTime) {
        if (pullRefreshView != null) {
            pullRefreshView.onPullFinished();
            forceRefreshToState(false, delayTime);
        }
    }

    /**
     * 设置刷新视图
     * @param pullView
     */
    public void setPullView(View pullView) {
        addOrUpdatePullView(pullHelper.getHeight(), pullView);
    }

    /**
     * 获取当前的状态
     * @return
     */
    public IPullRefreshView.State getPullState() {
        return pullState;
    }

    /**
     * 判断当前是否正在刷新
     * @return
     */
    public boolean isRefreshing() {
        return pullState == IPullRefreshView.State.MOVE_REFRESH;
    }

    /**
     * 获取当前的刷新视图对象
     * @return
     */
    public IPullRefreshView getPullRefreshView() {
        return pullRefreshView;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    protected final PullAnimationListener refreshingListener = new PullAnimationListener() {
        @Override
        public void start(Animation animation) {
            pullRefreshView.onPullRefresh();
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onRefresh();
            }
        }

        @Override
        public void end(Animation animation) {
        }
    };

    protected final PullAnimationListener resetListener = new PullAnimationListener() {
        @Override
        public void start(Animation animation) {
            pullState = IPullRefreshView.State.MOVE_SRPINGBACK;
        }

        @Override
        public void end(Animation animation) {
            if (pullHelper.getScroll() == 0) {
                pullState = IPullRefreshView.State.GONE;
                pullRefreshView.onPullHided();
            }
        }
    };

    protected final Runnable delayAnimToStartPosTask = new Runnable() {
        @Override
        public void run() {
            animToStartPosition(pullHelper.getScroll(), resetListener, 0);
        }
    };

    public static abstract class PullAnimation extends Animation {
        private int from;
        private int to;
        private int animDuration;

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int targetTop = (int) (from + (to - from) * interpolatedTime);
            applyTransformationTop(targetTop);
        }

        public abstract void applyTransformationTop(int targetTop);

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getAnimDuration() {
            return animDuration;
        }

        public void setAnimDuration(int animDuration) {
            this.animDuration = animDuration;
        }
    }

    public static abstract class PullAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            start(animation);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            end(animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        public abstract void start(Animation animation);

        public abstract void end(Animation animation);

    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
