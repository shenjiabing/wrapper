package com.sayweee.wrapper.bean;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/26.
 * Desc:
 */
public class NBean implements N {

    public String raw;

    @Override
    public void setRawData(String raw) {
        this.raw = raw;
    }

    public boolean isEmpty() {
        return raw == null || raw.length() == 0;
    }

    public String getRawData() {
        return raw;
    }
}
