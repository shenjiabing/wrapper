package com.sayweee.wrapper.widget;


import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

public class ColorDrawable extends Drawable {
    private Paint mPaint;
    private int color;
    private RectF rectF;
    private int rx, ry;

    public ColorDrawable() {
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
    }

    public ColorDrawable(int color) {
        this.color = color;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public ColorDrawable setColor(@ColorInt int color) {
        this.color = color;
        return this;
    }

    public ColorDrawable setRoundCorner(int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
        return this;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        rectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(color);
        canvas.drawRoundRect(rectF, rx, ry, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
