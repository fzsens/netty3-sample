package com.sino.event;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public interface TaskController {

    void start();

    void stop();

    void shutdown();

    boolean isStop();

    boolean isShutdown();

    boolean isRunning();
}
