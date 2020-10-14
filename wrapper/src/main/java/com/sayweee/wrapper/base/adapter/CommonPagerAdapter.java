package com.sayweee.wrapper.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.viewpager.widget.PagerAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/8/4.
 * Desc:
 */
public abstract class CommonPagerAdapter<T> extends PagerAdapter {

    protected List<T> adapterData;
    protected WeakReference<Context> contextRef;
    private int layoutRes;

    public CommonPagerAdapter(Context context, List<T> adapterData) {
        this(context, 0, adapterData);
    }

    public CommonPagerAdapter(Context context, @LayoutRes int layoutRes, List<T> adapterData) {
        this.adapterData = adapterData;
        this.contextRef = new WeakReference(context);
        this.layoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        return adapterData == null ? 0 : adapterData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T data = adapterData.get(position);
        View contentView = null;
        if (layoutRes != 0) {
            contentView = View.inflate(container.getContext(), layoutRes, null);
        }
        View view = bindView(contextRef.get(), contentView, data);
        container.addView(view);
        return view;
    }

    /**
     * 保定数据到内容view
     *
     * @param context
     * @param contentView 根据resourceId inflate出来的View,只有在resourceId不为0的情况下才有值
     * @param dataVo      position对应的数据对象
     * @return 绑定好数据及其他操作的的View
     */
    protected abstract View bindView(Context context, View contentView, T dataVo);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    public void setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public List<T> getAdapterData() {
        return adapterData;
    }

    public void setAdapterData(List<T> adapterData) {
        this.adapterData = adapterData;
    }
}
