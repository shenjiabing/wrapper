package com.sayweee.wrapper.base.adapter;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Author:  winds
 * Data:    2016/10/17
 * Version: 1.0
 * Desc:
 */
public class TabAdapter extends FragmentPagerAdapter {
    private int position;

    private List<Fragment> list;

    public TabAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.list = list;
    }

    public TabAdapter(FragmentManager fm, Fragment... fragments) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.list = arrayToList(fragments);
    }

    public Fragment getCurrentFragment() {
        return list.get(position);
    }

    public int getCurrentPosition() {
        return position;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        this.position = position;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    private List<Fragment> arrayToList(Fragment... fragments) {
        if (fragments != null && fragments.length > 0) {
            List<Fragment> list = new ArrayList<>();
            for (Fragment fragment : fragments) {
                list.add(fragment);
            }
            return list;
        }
        return null;
    }
}
