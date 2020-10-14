package com.sayweee.app2.test;

import com.sayweee.wrapper.bean.N;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/10/12.
 * Desc:
 */
public class NResponseBean implements N {

    public String raw;

    @Override
    public void setRawData(String raw) {
        this.raw = raw;
    }
}
