package com.sino.event;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public interface EventEmitter {

    void on(String eventName, EventHandler handler);

    /**
     * �����¼�
     * @param eventName �¼���
     * @param args ����
     */
    void emit(String eventName, Object... args);

    void remove(String eventName);
}
