package com.sayweee.widget.tabbar;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import androidx.core.view.ViewCompat;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2018/9/5.
 * Desc:
 */
public class TabBar extends LinearLayout {
    protected static final int TRANSLATE_DURATION_MILLIS = 200;

    protected final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    protected boolean mVisible = true;

    protected int linkPos;
    protected View linkView;
    protected boolean jumpEnable;
    protected LayoutParams tabParams;
    protected int currentPosition = 0;
    protected OnTabSelectedListener listener;
    protected long lastTime;
    protected int interval; //拦截间隔
    protected boolean intercept;    //是否拦截快速点击

    public TabBar(Context context) {
        this(context, null);
    }

    public TabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        tabParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        tabParams.weight = 1;
    }

    public TabBar addItem(final TabItem tab) {
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && !isInvalidClick()) {
                    int pos = tab.getTabPosition();
                    if (currentPosition == pos) {  //重选
                        listener.onTabRetrySelected(pos);
                    } else {
                        if (!listener.beforeTabSelected(pos, currentPosition)) { //点击前拦截
                            if (linkView != null && linkPos == pos && jumpEnable) { //选中依附的View 且依附View不需要当前tab状态变化
                                listener.onTabSelected(pos, currentPosition);
                            } else {
                                listener.onTabSelected(pos, currentPosition);
                                if (linkView != null && linkPos == pos) { //判断有没有需要依附的View
                                    linkView.setSelected(true);
                                } else {
                                    tab.setSelected(true);
                                }
                                listener.onTabUnselected(currentPosition);
                                if (linkView != null && linkPos == currentPosition) {
                                    linkView.setSelected(false);
                                } else {
                                    setCurrentItem(currentPosition, false);
                                }
                                currentPosition = pos;
                            }
                        }
                    }
                }
            }
        });
        tab.setTabPosition(getChildCount());
        tab.setLayoutParams(tabParams);
        addView(tab);
        return this;
    }


    /**
     * 设置快速点击拦截
     *
     * @param intercept
     * @param interval
     * @return
     */
    public TabBar setInterceptEnable(boolean intercept, int interval) {
        this.intercept = intercept;
        this.interval = interval;
        return this;
    }

    /**
     * 判断是否是快速点击
     *
     * @return
     */
    protected boolean isInvalidClick() {
        if (intercept) {
            long time = System.currentTimeMillis();
            if (System.currentTimeMillis() - lastTime < interval) {
                return true;
            } else {
                lastTime = time;
            }
        }
        return false;
    }

    /**
     * 设置依附的View 需要提前设置空Tab用于占位依附
     *
     * @param linkView
     * @param linkPos
     * @return
     */
    public TabBar link(View linkView, final int linkPos) {
        return link(linkView, linkPos, false);
    }

    /**
     * 设置依附的View 需要提前设置空Tab用于占位依附
     *
     * @param linkView
     * @param linkPos
     * @param jumpEnable 点击依附的View是否切换页面   true 不选中，避免选中效果  用于跳转新页面而不是切换tab
     * @return
     */
    public TabBar link(View linkView, final int linkPos, boolean jumpEnable) {
        this.linkView = linkView;
        this.linkPos = linkPos;
        this.jumpEnable = jumpEnable;
        linkView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(linkPos);
            }
        });
        return this;
    }

    /**
     * 设置当前的Tab反选
     *
     * @param position
     */
    public void setCurrentItem(final int position) {
        post(new Runnable() {
            @Override
            public void run() {
                getChildAt(position).performClick();
            }
        });
    }

    /**
     * 设置当前的Tab是否选中
     *
     * @param position
     * @param selected
     */
    public void setCurrentItem(final int position, final boolean selected) {
        post(new Runnable() {
            @Override
            public void run() {
                getChildAt(position).setSelected(selected);
            }
        });
    }

    /**
     * 获取当前选中的Tab位置
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 获取 Tab
     */
    public TabItem getItem(int index) {
        if (getChildCount() <= index) return null;
        return (TabItem) getChildAt(index);
    }

    public int getCount() {
        return getChildCount();
    }

    public TabBar setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        listener = onTabSelectedListener;
        return this;
    }

    public void hide() {
        hide(true);
    }

    public void show() {
        show(true);
    }

    public void hide(boolean anim) {
        toggle(false, anim, false);
    }

    public void show(boolean anim) {
        toggle(true, anim, false);
    }

    public boolean isVisible() {
        return mVisible;
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height;
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewCompat.setTranslationY(this, translationY);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedState(superState, currentPosition);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        if (currentPosition != ss.position) {
            getChildAt(currentPosition).setSelected(false);
            getChildAt(ss.position).setSelected(true);
        }
        currentPosition = ss.position;
    }

    static class SavedState extends BaseSavedState {
        private int position;

        public SavedState(Parcel source) {
            super(source);
            position = source.readInt();
        }

        public SavedState(Parcelable superState, int position) {
            super(superState);
            this.position = position;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

