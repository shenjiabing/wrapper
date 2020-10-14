package com.sayweee.wrapper.core.ex.bus;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/9/25.
 * Desc:
 */
public class BusEvent {

    public int code;
    public IEvent event;

    public BusEvent(int code, IEvent event) {
        this.code = code;
        this.event = event;
    }
}
