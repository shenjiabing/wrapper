package com.sayweee.wrapper.core.binding.command;


public interface BindingConsumer<T> {
    void call(T t);
}
