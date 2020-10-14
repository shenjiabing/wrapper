package com.sayweee.widget.tabbar;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2018/9/5.
 * Desc:
 */
public interface OnTabSelectedListener {
    /**
     * Tab被选中前回调，若返回true，则可消费掉当前事件
     *
     * @param position
     * @return
     */
    boolean beforeTabSelected(int position, int prePosition);

    /**
     * 当前Tab被选中时的回调，可用于页面切换
     *
     * @param position
     * @param prePosition
     */
    void onTabSelected(int position, int prePosition);

    /**
     * 当前Tab被切换时的上一个被选中的位置
     *
     * @param position
     */
    void onTabUnselected(int position);

    /**
     * 当前Tab已被选中，再次点击时的回调
     *
     * @param position
     */
    void onTabRetrySelected(int position);
}
