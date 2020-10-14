package com.sayweee.wrapper.base.adapter;

import java.util.List;

/**
 * Author:  winds
 * Data:    2016/10/17
 * Version: 1.0
 * Desc:    适配器选择方法接口
 */
public interface Selectable<T> {

    /**
     * 判断当前是否被选中
     * @param item
     * @return
     */
    boolean isSelected(T item);

    /**
     * 切换当前选中效果
     * @param item
     * @return  当前是否被选中
     */
    boolean toggleSelection(T item);

    /**
     * 清楚所有的选中
     */
    void clearSelection();

    /**
     * 获取选中的数目
     * @return
     */
    int getSelectedCount();

    /**
     * 获取所有的选中条目
     * @return
     */
    List<T> getSelection();
}
