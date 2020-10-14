package com.sayweee.wrapper.base.adapter;

import android.util.ArrayMap;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/8/4.
 * Desc:
 */
public abstract class CommonFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    protected List<T> adapterData;
    protected Map<Integer, Fragment> viewData = new HashMap<>();

    public CommonFragmentPagerAdapter(FragmentManager fm, List<T> adapterData) {
        super(fm);
        this.adapterData = adapterData;
    }

    @Override
    public Fragment getItem(int position) {
        T data = adapterData.get(position);
        Fragment fragment = null;
        if (viewData.containsKey(position)) {
            fragment = viewData.get(position);
        }

        if (fragment == null) {
            fragment = initFragment(data, position);
            viewData.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return adapterData == null ? 0 : adapterData.size();
    }

    protected abstract Fragment initFragment(T data, int position);

    public List<T> getAdapterData() {
        return adapterData;
    }

    public void setAdapterData(List<T> dataList) {
        this.adapterData = dataList;
    }

    public Map<Integer, Fragment> getViewData() {
        return viewData;
    }
}
