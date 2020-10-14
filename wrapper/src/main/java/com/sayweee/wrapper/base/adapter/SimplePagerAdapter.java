package com.sayweee.wrapper.base.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/8/6.
 * Desc:
 */
public class SimplePagerAdapter extends PagerAdapter {
    List<View> adapterData;

    public SimplePagerAdapter(List<View> adapterData) {
        this.adapterData = adapterData;
    }

    public void setAdapterData(List<View> adapterData) {
        this.adapterData = adapterData;
    }

    @Override
    public int getCount() {
        return adapterData == null ? 0 : adapterData.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = adapterData.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }
}
