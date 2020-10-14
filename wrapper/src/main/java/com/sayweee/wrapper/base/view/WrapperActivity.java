package com.sayweee.wrapper.base.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sayweee.wrapper.R;
import com.sayweee.wrapper.widget.TitleView;
import com.sayweee.wrapper.widget.Toaster;


/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/20.
 * Desc:
 */
public abstract class WrapperActivity extends AppCompatActivity {
    private boolean isAutoFocus = true;  //自动显示和隐藏输入法
    private boolean isClearFocus = true;  //是否自动清除焦点
    private long lastTime;
    private boolean interceptAble;  //是否拦截快速点击事件

    protected Activity activity;
    protected FrameLayout container;
    protected View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeOnCreate();
        setContentView(useWrapper() ? R.layout.activity_wrapper : getLayoutRes());
        init();
        initView(savedInstanceState, getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //快速点击拦截
        if (!interceptAble && (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN)) {
            if (isInvalidClick()) {
                return true;
            }
        }
        //键盘拦截判断
        if (isAutoFocus) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideInput(v, ev)) {
                    if (isClearFocus && v != null) {
                        v.clearFocus();
                    }
                    setKeyboardVisible(v, false);
                }
                return super.dispatchTouchEvent(ev);
            }
            // 其他组件响应点击事件
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void init() {
        activity = this;
        if (useWrapper()) {
            container = findViewById(R.id.container);
            getTitleView().setChildClickListener(R.id.iv_title_left, getBackClickListener());
            initContentView();
        } else {
            container = findViewById(android.R.id.content);
            contentView = container.getChildAt(0);
        }
        initStatusBar();
    }

    protected boolean useStatusBarDefault() {
        return true;
    }

    protected void initStatusBar() {
        if (useStatusBarDefault()) {
            setStatusBar();
        }
    }

    protected void setStatusBar() {

    }

    protected void initContentView() {
        if (getLayoutRes() > 0) {
            contentView = View.inflate(this, getLayoutRes(), null);
            container.addView(contentView);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    protected void beforeOnCreate() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //去除默认actionbar
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//统一管理竖屏
    }

    /**
     * 返回getLayoutRes所inflate的view
     *
     * @return
     */
    protected View getContentView() {
        return contentView;
    }

    /**
     * 返回装载getLayoutRes所inflate的view
     *
     * @return
     */
    protected View getContainerView() {
        return container;
    }

    /**
     * 标题返回按钮 如需重写 覆盖此方法即可
     *
     * @return
     */
    protected View.OnClickListener getBackClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    /**
     * 设置标题和内容之间的分隔线 仅在使用useWrapper = true时生效
     * 若图片不存在，则取消分隔线
     * @param drawableRes
     */
    protected void setTitleDivider(@DrawableRes int drawableRes) {
        setTitleDivider(getResources().getDrawable(drawableRes));
    }

    /**
     * 设置标题和内容之间的分隔线 仅在使用useWrapper = true时生效
     * 若设置成null，则取消分隔线
     * @param drawable
     */
    protected void setTitleDivider(Drawable drawable) {
        if (useWrapper()) {
            LinearLayout view = findViewById(R.id.root);
            view.setDividerDrawable(drawable);
        }
    }

    /**
     * 设置中间标题
     * @param title
     */
    protected void setWrapperTitle(CharSequence title) {
        if (useWrapper()) {
            getTitleView().setTitle(title);
        }
    }

    /**
     * 设置中间标题
     * @param stringRes
     */
    protected void setWrapperTitle(@StringRes int stringRes) {
        setWrapperTitle(getResources().getText(stringRes));
    }

    /**
     * 获取标题
     * @return
     */
    protected TitleView getTitleView() {
        if(useWrapper()) {
            return findViewById(R.id.title);
        }
        return null;
    }

    /**
     * 设置是否拦截快速点击
     *
     * @param interceptAble 默认拦截   设置不拦截请设置为 false
     */
    protected void setInterceptAble(boolean interceptAble) {
        this.interceptAble = !interceptAble;
    }

    /**
     * 判断是否是快速点击
     *
     * @return
     */
    private boolean isInvalidClick() {
        long time = System.currentTimeMillis();
        long duration = time - lastTime;
        if (duration < 400) {
            return true;
        } else {
            lastTime = time;
            return false;
        }
    }

    public void setClearFocus(boolean isClearFocus) {
        this.isClearFocus = isClearFocus;
    }

    /**
     * 判断键盘是否应该隐藏
     * 点击除EditText的区域隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置键盘显否
     *
     * @param v
     * @param visible
     */
    protected void setKeyboardVisible(View v, boolean visible) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (v == null) {
                v = activity.getWindow().getDecorView();
            }
            if (v != null) {
                if (visible) {
                    imm.showSoftInput(v, 0);
                } else {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
    }

    /**
     * 设置自动隐藏输入法
     *
     * @param isAutoFocus 默认 true 自动隐藏
     */
    protected void setAutoFocus(boolean isAutoFocus) {
        this.isAutoFocus = isAutoFocus;
    }

    /**
     * 普通Toast提示
     *
     * @param msg
     */
    protected void showToast(String msg) {
        Toaster.showToast(msg);
    }

    /**
     * 是否使用通用包装 不使用会移除title支持 使用原始的构造
     *
     * @return
     */
    protected boolean useWrapper() {
        return true;
    }

    /**
     * 当useWrapper=true 若 getLayoutRes < 0 会被忽略
     *
     * @return
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化
     *
     * @param savedInstanceState
     * @param intent
     */
    protected abstract void initView(Bundle savedInstanceState, Intent intent);

}
