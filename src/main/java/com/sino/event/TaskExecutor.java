package com.sino.event;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public interface TaskExecutor extends Task {
    void submit(Task task);
}
