package com.sayweee.wrapper.base.view;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sayweee.wrapper.R;
import com.sayweee.wrapper.utils.KeyboardUtils;
import com.sayweee.wrapper.widget.TitleView;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public abstract class WrapperActivity extends AppCompatActivity implements IView, IWrapper {
    protected Activity activity;
    protected View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeCreate();
        setContentView();
        initView(getView(), savedInstanceState);
        loadData();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoKeyboardEnable()) {//键盘拦截判断
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    if (v != null) {
                        v.clearFocus();
                    }
                    KeyboardUtils.setKeyboardVisible(this, v, false);
                }
                return super.dispatchTouchEvent(ev);
            }
            // 其他组件响应点击事件
            try {
                if (getWindow().superDispatchTouchEvent(ev)) {
                    return true;
                }
            }catch (Exception e){}
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 是否使用通用包装 不使用会移除wrapper支持 使用原始的构造
     *
     * @return
     */
    @Override
    public boolean useWrapper() {
        return true;
    }

    @Override
    public View getView() {
        View view = findViewById(android.R.id.content);
        if (view instanceof FrameLayout && ((FrameLayout) view).getChildCount() > 0) {
            return ((FrameLayout) view).getChildAt(0);
        }
        return view;
    }

    /**
     * 返回getLayoutRes所inflate的view
     *
     * @return
     */
    @Override
    public View getContentView() {
        if(contentView == null) {
            contentView = getView();
        }
        return contentView;
    }

    /**
     * 获取标题
     *
     * @return
     */
    @Override
    public TitleView getWrapperTitle() {
        if (useWrapper()) {
            return findViewById(R.id.layout_wrapper_title);
        }
        return null;
    }

    /**
     * 设置标题和内容之间的分隔线 仅在使用useWrapper = true时生效
     * 若设置成null，则取消分隔线
     *
     * @param drawable
     */
    @Override
    public void setWrapperDivider(Drawable drawable) {
        if (useWrapper()) {
            LinearLayout view = findViewById(R.id.layout_wrapper_root);
            view.setDividerDrawable(drawable);
        }
    }

    /**
     * 设置中间标题
     *
     * @param title
     */
    @Override
    public void setWrapperTitle(CharSequence title) {
        if (useWrapper()) {
            getWrapperTitle().setTitle(title);
        }
    }

    /**
     * 设置中间标题
     *
     * @param stringRes
     */
    @Override
    public void setWrapperTitle(@StringRes int stringRes) {
        setWrapperTitle(getResources().getText(stringRes));
    }

    public void beforeCreate() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //去除默认actionbar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//统一管理竖屏
    }

    protected void setContentView() {
        this.activity = this;
        if (useWrapper()) {
            setContentView(R.layout.activity_wrapper);
            FrameLayout container = findViewById(R.id.layout_wrapper_container);
            if (getLayoutRes() > 0) {
                contentView = View.inflate(this, getLayoutRes(), null);
                container.addView(contentView);
            }
        } else {
            setContentView(getLayoutRes());
            contentView = getView();
        }
        if (useStatusBarWrapper()) {
            initStatusBar();
        }
    }

    /**
     * 是否开启状态栏自动适配
     * 开启后，仅在{@useWrapper}模式下生效
     *
     * @return
     */
    protected boolean useStatusBarWrapper() {
        return true;
    }

    /**
     * 初始化状态栏
     * 可重写此方法实现定制需求
     */
    protected void initStatusBar() {
        if (useWrapper()) {

        }
    }

    /**
     * 判断键盘是否应该隐藏
     * 点击除EditText的区域隐藏
     *
     * @param v
     * @param event
     * @return
     */
    protected boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] location = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(location);
            int left = location[0];
            int top = location[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public View.OnClickListener getOnBackListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    @Override
    public boolean autoKeyboardEnable() {
        return true;
    }
}
