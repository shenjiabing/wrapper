package com.sayweee.wrapper.listener;

import android.view.View;

/**
 * Author:  winds
 * Data:    2016/6/11
 * Version: 1.0
 * Desc:
 */
public interface OnAdapterChildClickListener {

    /**
     * 点击事件回调
     * @param view      触发的view
     * @param position  触发的position
     */
    void onAdapterChildClick(View view, int position);

}
