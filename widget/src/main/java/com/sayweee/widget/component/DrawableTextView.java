package com.sayweee.widget.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.sayweee.widget.R;

public class DrawableTextView extends androidx.appcompat.widget.AppCompatTextView {

    private int drawableWidth;
    private int drawableHeight;

    private Drawable leftDrawable;
    private Drawable topDrawable;
    private Drawable rightDrawable;
    private Drawable bottomDrawable;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DrawableTextView, 0, 0);
        int countNum = ta.getIndexCount();
        for (int i = 0; i < countNum; i++) {
            int attr = ta.getIndex(i);
            if (attr == R.styleable.DrawableTextView_leftDrawable) {
                leftDrawable = ta.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_topDrawable) {
                topDrawable = ta.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_rightDrawable) {
                rightDrawable = ta.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_bottomDrawable) {
                bottomDrawable = ta.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_drawableWidth) {
                drawableWidth = ta.getDimensionPixelSize(R.styleable.DrawableTextView_drawableWidth, 0);
            } else if (attr == R.styleable.DrawableTextView_drawableHeight) {
                drawableHeight = ta.getDimensionPixelSize(R.styleable.DrawableTextView_drawableHeight, 0);
            }
        }
        ta.recycle();
        init();
    }

    /**
     * init views
     */
    private void init() {
        setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, getDrawableWidth(left), getDrawableHeight(left));
        }

        if (top != null) {
            top.setBounds(0, 0, getDrawableWidth(top), getDrawableHeight(top));
        }

        if (right != null) {
            right.setBounds(0, 0, getDrawableWidth(right), getDrawableHeight(right));
        }

        if (bottom != null) {
            bottom.setBounds(0, 0, getDrawableWidth(bottom), getDrawableHeight(bottom));
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * 获取drawable宽度
     *
     * @param drawable
     * @return
     */
    public int getDrawableWidth(Drawable drawable) {
        if (drawable != null) {
            if (drawableWidth <= 0) {
                return drawable.getMinimumWidth();
            }
            return drawableWidth;
        }
        return 0;
    }

    /**
     * 获取drawable高度
     *
     * @param drawable
     * @return
     */
    public int getDrawableHeight(Drawable drawable) {
        if (drawable != null) {
            if (drawableHeight <= 0) {
                return drawable.getMinimumHeight();
            }
            return drawableHeight;
        }
        return 0;
    }

    /**
     * 设置drawable大小，配合{@setDrawableBounds}使用
     *
     * @param drawable
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setDrawableBounds(Drawable drawable, int left, int top, int right, int bottom) {
        if (drawable != null) {
            drawable.setBounds(left, top, right, bottom);
        }
    }

    /**
     * 设置drawable, 代码设置drawable需要调用{@setDrawableBounds}，其自定义大小设置才会生效
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * 设置通过自定义属性设置的drawable的bounds
     *
     * @param width
     * @param height
     */
    public void resetDrawableBounds(int width, int height) {
        this.drawableWidth = width;
        this.drawableHeight = height;
        init();
    }
}

