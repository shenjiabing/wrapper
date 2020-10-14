package com.sayweee.widget.tabbar;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2018/9/5.
 * Desc:
 */
public abstract class OnSimpleTabSelectedListener implements OnTabSelectedListener {

    @Override
    public boolean beforeTabSelected(int position, int prePosition) {
        return false;
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabRetrySelected(int position) {

    }
}