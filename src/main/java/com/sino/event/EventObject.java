package com.sino.event;

/**
 * an event.
 * Created by thierry.fu on 2017/1/3.
 */
public class EventObject {
    final private String eventName;
    final private Object source;
    final private Object args[];

    public EventObject(String eventName,Object source) {
        this(eventName,source,null);
    }

    public EventObject(String eventName,Object source,Object[] args) {
        this.eventName = eventName;
        this.source = source;
        this.args = args;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getSource() {
        return source;
    }

    public Object[] getArgs() {
        return args;
    }
}
