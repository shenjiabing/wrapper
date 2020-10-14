package com.sayweee.widget.refresh;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sayweee.widget.R;


/**
 * Author by winds on 2016/12/15
 * Email heardown@163.com
 */
public class DefaultRefreshView extends LinearLayout implements IPullRefreshView {

    private static final int ANIMATION_DURATION = 150;
    private static final Interpolator ANIMATION_INTERPOLATOR = new DecelerateInterpolator();

    private Animation mRotateAnimation;
    private Animation mResetRotateAnimation;
    private AnimationDrawable animationDrawable;


    ImageView indicator;
    ImageView progress;
    TextView msg;

    public DefaultRefreshView(Context context) {
        this(context, null);
    }

    public DefaultRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        FrameLayout layout = new FrameLayout(context);
        LayoutParams parentParams = new LayoutParams(dp2px(34), dp2px(34));

        indicator = new ImageView(context);
        indicator.setImageResource(R.drawable.ic_refresh_arrow);
        FrameLayout.LayoutParams indicatorParams = new FrameLayout.LayoutParams(dp2px(28), dp2px(28));
        indicatorParams.gravity = Gravity.CENTER;
        layout.addView(indicator, indicatorParams);

        progress = new ImageView(context);
        FrameLayout.LayoutParams progressParams = new FrameLayout.LayoutParams(dp2px(34), dp2px(34));
        progressParams.gravity = Gravity.CENTER;
        layout.addView(progress, progressParams);
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.ic_refresh_loading);
        progress.setImageDrawable(animationDrawable);
        progress.setVisibility(INVISIBLE);
        addView(layout, parentParams);

        msg = new TextView(context);
        LayoutParams msgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        msgParams.leftMargin = dp2px(8);
        msg.setTextSize(12);
        msg.setTextColor(Color.parseColor("#777777"));
        addView(msg, msgParams);

        initAnimation();
    }

    private void initAnimation() {
        mRotateAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mResetRotateAnimation.setDuration(0);
        mResetRotateAnimation.setFillAfter(true);
    }

    //隐藏
    @Override
    public void onPullHided() {
        indicator.setVisibility(VISIBLE);

        progress.clearAnimation();
        progress.setVisibility(INVISIBLE);
        msg.setText(R.string.pulling);
    }

    //刷新中
    @Override
    public void onPullRefresh() {
        indicator.clearAnimation();
        indicator.setVisibility(INVISIBLE);

        progress.setVisibility(VISIBLE);
        animationDrawable.start();
        msg.setText(R.string.pulling_refreshing);
    }

    //提示松手
    @Override
    public void onPullFreeHand() {
        indicator.setVisibility(VISIBLE);
        if (indicator.getAnimation() == null || indicator.getAnimation() == mResetRotateAnimation) {
            indicator.startAnimation(mRotateAnimation);
        }
        progress.setVisibility(INVISIBLE);
        msg.setText(R.string.pulling_refresh);
    }

    //下啦中
    @Override
    public void onPullDowning() {
        indicator.setVisibility(VISIBLE);
        if (indicator.getAnimation() == null || indicator.getAnimation() == mRotateAnimation) {
            mResetRotateAnimation.setDuration(150);
            indicator.startAnimation(mResetRotateAnimation);
        }

        progress.setVisibility(INVISIBLE);
        msg.setText(R.string.pulling);
    }

    //刷新完成
    @Override
    public void onPullFinished() {
        indicator.setImageResource(R.drawable.ic_refresh_arrow);
        indicator.setVisibility(VISIBLE);
        animationDrawable.stop();
        progress.setVisibility(INVISIBLE);
        msg.setText(R.string.pulling_refresh_finish);
    }

    @Override
    public void onPullProgress(float pullDistance, float pullProgress) {

    }

    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
    }
}
