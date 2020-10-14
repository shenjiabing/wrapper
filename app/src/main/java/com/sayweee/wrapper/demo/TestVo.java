package com.sayweee.wrapper.demo;

import com.sayweee.logger.Logger;
import com.sayweee.wrapper.core.model.BaseVo;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/26.
 * Desc:
 */
public class TestVo extends BaseVo {

    @Override
    public Type elementType() {
        return super.elementType();
    }

    @Override
    public void setDataList(List<?> list) {
        super.setDataList(list);
        Logger.json(list);
    }

}
