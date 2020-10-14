package com.sayweee.wrapper.listener;

import android.view.View;

/**
 * Author:  winds
 * Data:    2016/6/11
 * Version: 1.0
 * Desc:
 */
public interface OnAdapterClickListener {

    /**
     * 点击回调
     * @param view      触发的view
     * @param position  触发的position
     */
    void onAdapterClick(View view, int position);

}
