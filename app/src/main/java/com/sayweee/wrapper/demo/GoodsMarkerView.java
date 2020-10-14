package com.sayweee.wrapper.demo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/22.
 * Desc:
 */
public class GoodsMarkerView extends View {

    protected Paint rectPaint;      //绘制四边形
    protected Paint trianglePaint;  //绘制三角形
    protected Paint textPaint;      //绘制文字

    protected int ratio;
    protected int textY;            //文字的y的坐标
    protected int textSize;         //文字的大小

    protected Path trianglePath;    //绘制三角形
    protected Path rectPath;        //绘制上层矩形

    protected String text;          //要显示的文字

    public GoodsMarkerView(Context context) {
        this(context, null);
    }

    public GoodsMarkerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodsMarkerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        ratio = dp2px(6);
        textSize = sp2px(13);

        rectPaint = new Paint();
        rectPaint.setColor(Color.parseColor("#df2c2e"));
        rectPaint.setAntiAlias(true);

        trianglePaint = new Paint();
        trianglePaint.setColor(Color.parseColor("#C10D0E"));
        trianglePaint.setAntiAlias(true);

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setFakeBoldText(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(text)) {
            canvas.drawPath(rectPath, rectPaint);

            canvas.drawText(text, ratio, textY, textPaint);

            canvas.drawPath(trianglePath, trianglePaint);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        retryCalcSize();
    }

    private void retryCalcSize() {
        int rectHeight = textSize + ratio;
        int triangleHeight = ratio;
        int rectWidth = calcTextWidth(textPaint, text) + ratio + ratio / 2;

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textY = (int) ((rectHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);

        rectPath = new Path();
        rectPath.moveTo(0, 0);
        rectPath.lineTo(rectWidth + ratio, 0);
        rectPath.lineTo(rectWidth, rectHeight);
        rectPath.lineTo(0, rectHeight);


        trianglePath = new Path();
        trianglePath.moveTo(0, rectHeight);
        trianglePath.lineTo(triangleHeight, rectHeight);
        trianglePath.lineTo(triangleHeight, rectHeight + triangleHeight);
        trianglePath.close();
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

    /**
     * sp转px
     *
     * @param spVal
     * @return
     */
    public int sp2px(float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, Resources.getSystem().getDisplayMetrics());
    }

    public int calcTextWidth(Paint paint, String text) {
        if (TextUtils.isEmpty(text)) {
            return 0;
        }
        return (int) (paint.measureText(text) + 0.5);
    }

    public void setText(String text) {
        this.text = text;
        retryCalcSize();
        invalidate();
    }

    /**
     * 设置
     *
     * @param text
     * @param colorRect     上层矩形的颜色
     * @param colorTriangle 下层三角形的颜色
     */
    public void setText(String text, @ColorInt int colorRect, @ColorInt int colorTriangle) {
        rectPaint.setColor(colorRect);
        trianglePaint.setColor(colorTriangle);
        setText(text);
    }

    /**
     * 设置文字的大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
        retryCalcSize();
        invalidate();
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        textPaint.setColor(color);
        invalidate();
    }

    public void setTextGreen(String text) {
        setText(text, Color.parseColor("#62ad16"), Color.parseColor("#348304"));
    }

    public void setTextRed(String text) {
        setText(text, Color.parseColor("#df2c2e"), Color.parseColor("#c10d0e"));
    }

    public void setTextBlue(String text) {
        setText(text, Color.parseColor("#008ed6"), Color.parseColor("#0060b5"));
    }
}
