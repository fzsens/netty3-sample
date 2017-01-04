package com.sino.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public abstract class TaskEventEmitter implements Task, EventEmitter{
    private Map<String,List<EventHandler>> eventHandlerMap = new HashMap<String, List<EventHandler>>();

    private final TaskExecutor executor;

    public TaskEventEmitter(TaskExecutor executor){
        this.executor = executor;
        addDefaultExceptionHandlers();
    }

    protected void addDefaultExceptionHandlers() {
        EventHandler eh = event -> ((Exception)event.getSource()).printStackTrace();
        on(Exception.class.getName(),eh);
        on(IOException.class.getName(),eh);
        on(RuntimeException.class.getName(),eh);
        on(NullPointerException.class.getName(),eh);
        on(IndexOutOfBoundsException.class.getName(),eh);
    }

    @Override
    public void on(String eventName, EventHandler handler) {
        if(!eventHandlerMap.containsKey(eventName)) {
            eventHandlerMap.put(eventName, new LinkedList<>());
        }
        eventHandlerMap.get(eventName).add(handler);
    }

    @Override
    public void remove(String eventName) {
        eventHandlerMap.remove(eventName);
    }

    @Override
    public void emit(String eventName, Object... args) {
        if(eventHandlerMap.containsKey(eventName)) {
            List<EventHandler> eventHandlerList = eventHandlerMap.get(eventName);
            for(final EventHandler handler : eventHandlerList) {
                executor.submit(() -> {
                    //TaskEventEmitter.this
                    handler.handle(new EventObject(eventName,TaskEventEmitter.this,args));
                });
            }
        } else {
            if(args[0] instanceof Exception) {
                ((Exception)args[0]).printStackTrace();
            }
        }
    }

    @Override
    public void execute() {
        try {
            run();
        } catch (Exception e) {
            emit(e.getClass().getName(),e);
        }
    }

    /**
     * »ŒŒÒ÷¥––
     * @throws Exception
     */
    protected abstract void run() throws Exception;

}
