package com.sayweee.app2.rx;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sayweee.app2.R;
import com.sayweee.wrapper.core.BaseViewModel;
import com.sayweee.wrapper.core.model.ILoaderModel;
import com.sayweee.wrapper.core.view.WrapperMvvmActivity;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/15.
 * Desc:
 */
public abstract class PanelActivity<VM extends BaseViewModel<? extends ILoaderModel>> extends WrapperMvvmActivity<VM> implements View.OnClickListener {
    LinearLayout container;
    LinearLayout.LayoutParams params;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_rx_test;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        container = findViewById(R.id.container);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(50));
        params.topMargin = dp2px(6);
        int margin = dp2px(12);
        params.leftMargin = margin;
        params.rightMargin = margin;
        initView();
    }

    @Override
    public void loadData() {

    }

    @Override
    public void attachModel() {

    }


    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (tag != null && tag instanceof String) {
            onClick(String.valueOf(tag));
        }
    }


    public void addItems(String... titles) {
        if (titles != null && titles.length > 0) {
            for (String item : titles) {
                addItem(item, this);
            }
        }
    }

    public void addItems(View.OnClickListener listener, String... titles) {
        if (titles != null && titles.length > 0) {
            for (String item : titles) {
                addItem(item, listener);
            }
        }
    }

    public void addItem(String msg, View.OnClickListener listener) {
        Button button = new Button(this);
        button.setTextColor(Color.WHITE);
        button.setTextSize(16);
        button.setText(msg);
        button.setTag(msg);
        button.setBackgroundResource(R.drawable.selector_btn_orange);
        container.addView(button, params);
        button.setOnClickListener(listener);
    }


    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, Resources.getSystem().getDisplayMetrics());
    }

    protected abstract void initView();

    public abstract void onClick(String tag);
}
