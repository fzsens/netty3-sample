package com.sino.event;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * task mgt
 * Created by thierry.fu on 2017/1/3.
 */
public class TaskManager implements TaskController {

    private TaskExecutor executor;
    private Thread executorThread;//work thread
    private int lenOfTaskQueue = Integer.MAX_VALUE;

    private boolean isStop = true;//标识执行器是否接收新的任务
    private boolean isShutdown = true;//标识执行器是否立刻结束
    private boolean isRunning = false;//执行器是否正在运行

    public TaskManager(){}

    public TaskManager(int lenOfTaskQueue){
        this.lenOfTaskQueue = lenOfTaskQueue;
    }

    public TaskManager(TaskExecutor executor) {
        this.executor = executor;
    }

    public TaskExecutor getExecutor() {
        if(executor == null) {
            init();
        }
        return executor;
    }

    private void init() {
        executor = new DefaultTaskExecutor(lenOfTaskQueue);
    }

    private class DefaultTaskExecutor implements TaskExecutor {

        private final BlockingDeque<Task> taskQueue;

        public DefaultTaskExecutor() {
            this(Integer.MAX_VALUE);
        }

        public DefaultTaskExecutor(int len){
            taskQueue = new LinkedBlockingDeque<>(len);
        }

        @Override
        public void submit(Task task) {
            if(!isStop) {
                try {
                    taskQueue.put(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalStateException("This executor is not alive");
            }
        }

        @Override
        public void execute() {
            isRunning = true;
            //clear
            taskQueue.clear();
            System.out.println("start process task ... ");
            while (!isShutdown && (!isStop || taskQueue.size() > 0)) {
                try {
                    //selector
                    Task task = taskQueue.take();
                    task.execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Stop to process task ... ");
            isRunning = false;
        }
    }

    @Override
    public void start() {
        if(executor == null) {
            init();
        }
        if(isStop && !isRunning) {
            isStop = false;
            isShutdown = false;
            executorThread = new Thread() {
                @Override
                public void run() {
                    executor.execute();
                }
            };
            executorThread.start();
        } else {
            throw new IllegalStateException("This executor is still alive or running.");
        }
    }

    @Override
    public void stop() {
        isStop = true;
        //中断执行线程
        executorThread.interrupt();
    }

    @Override
    public void shutdown() {
        isStop = true;
        isShutdown = true;
        executorThread.interrupt();
    }

    @Override
    public boolean isStop() {
        return this.isStop;
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}
