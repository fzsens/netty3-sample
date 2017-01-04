package com.sino.event;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public interface EventEmitter {

    void on(String eventName, EventHandler handler);

    /**
     * 触发事件
     * @param eventName 事件名
     * @param args 参数
     */
    void emit(String eventName, Object... args);

    void remove(String eventName);
}
