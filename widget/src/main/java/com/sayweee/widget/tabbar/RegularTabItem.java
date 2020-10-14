package com.sayweee.widget.tabbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.sayweee.widget.R;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2018/9/5.
 * Desc:
 */
public class RegularTabItem extends TabItem {

    protected View dot;
    protected TextView msg;
    protected TextView txt;
    protected ImageView image;
    protected ImageView expand;

    protected int position = -1;
    protected int defaultNormalIcon, defaultSelectedIcon;
    protected String defaultTitle;

    public RegularTabItem(@NonNull Context context) {
        this(context, null);
    }

    public RegularTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RegularTabItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_regular_tab, this, true);
        dot = findViewById(R.id.dot);
        msg = findViewById(R.id.msg);
        txt = findViewById(R.id.txt);
        image = findViewById(R.id.image);
        expand = findViewById(R.id.expand);
        txt.getPaint().setFakeBoldText(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
            Drawable drawable = typedArray.getDrawable(0);
            setBackground(drawable);
            typedArray.recycle();
        }
    }

    public RegularTabItem init(@DrawableRes int iconNormal, @DrawableRes int iconSelected, @NonNull String title, @ColorInt int colorNormal, @ColorInt int colorSelected) {
        setDefaultValue(iconNormal, iconSelected, title);
        init(iconNormal, iconSelected, title);
        setTextColor(colorNormal, colorSelected);
        return this;
    }

    /**
     * 设置默认icon
     *
     * @param iconNormal
     * @param iconSelected
     * @return
     */
    public RegularTabItem setDefaultValue(@DrawableRes int iconNormal, @DrawableRes int iconSelected, String text) {
        this.defaultNormalIcon = iconNormal;
        this.defaultSelectedIcon = iconSelected;
        this.defaultTitle = text;
        return this;
    }

    public RegularTabItem setDefaultState() {
        return init(defaultNormalIcon, defaultSelectedIcon, defaultTitle);
    }

    /**
     * 设置字体图片
     *
     * @param iconNormal
     * @param iconSelected
     * @param title
     * @return
     */
    public RegularTabItem init(@DrawableRes int iconNormal, @DrawableRes int iconSelected, @NonNull String title) {
        setStateDrawable(ContextCompat.getDrawable(getContext(), iconNormal), ContextCompat.getDrawable(getContext(), iconSelected));
        setText(title);
        return this;
    }

    private RegularTabItem setStateDrawable(Drawable iconNormal, Drawable iconSelected) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected}, iconSelected);//  选中状态, 设置按下的图片
        drawable.addState(new int[]{}, iconNormal);//默认状态,默认状态下的图片
        image.setImageDrawable(drawable);
        return this;
    }


    public RegularTabItem setDynamicIcon(Drawable iconNormal, Drawable iconSelected) {
        if (iconNormal == null) {
            iconNormal = ContextCompat.getDrawable(getContext(), defaultNormalIcon);
        }
        if (iconSelected == null) {
            iconSelected = ContextCompat.getDrawable(getContext(), defaultSelectedIcon);
        }
        return setStateDrawable(iconNormal, iconSelected);
    }

    public RegularTabItem setText(String text) {
        txt.setText(text);
        return this;
    }

    /**
     * 设置字体颜色
     *
     * @param colorNormal
     * @param colorSelected
     * @return
     */
    public RegularTabItem setTextColor(@ColorInt int colorNormal, @ColorInt int colorSelected) {
        int[] color = new int[]{colorSelected, colorNormal};
        int[][] state = new int[2][];
        state[0] = new int[]{android.R.attr.state_selected};
        state[1] = new int[]{};
        ColorStateList colors = new ColorStateList(state, color);
        txt.setTextColor(colors);
        return this;
    }


    @Override
    public void setTabPosition(int position) {
        this.position = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    @Override
    public int getTabPosition() {
        return position;
    }

    @Override
    public void setSelected(boolean selected) {
        image.setSelected(selected);
        txt.setSelected(selected);
    }

    @Override
    public void setBadgeMessage(int num) {
        if (num > 0) {
            msg.setVisibility(VISIBLE);
            if (num < 100) {
                msg.setText(String.valueOf(num));
            } else {
                msg.setText(String.valueOf(99));
            }
        } else {
            msg.setVisibility(GONE);
        }
    }

    @Override
    public void setBadgeDot(boolean visible) {
        dot.setVisibility(visible ? VISIBLE : GONE);
    }

    @Override
    public void setBadgeExpand(Drawable drawable) {
        if (drawable != null) {
            expand.setVisibility(VISIBLE);
            expand.setImageDrawable(drawable);
        } else {
            expand.setVisibility(GONE);
        }
    }

    @Override
    public void dismissBadge() {
        setBadgeMessage(0);
        setBadgeDot(false);
        setBadgeExpand(null);
    }
}
