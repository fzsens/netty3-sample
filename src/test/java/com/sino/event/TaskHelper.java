package com.sino.event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public class TaskHelper {

    private  TaskHelper() {}

    public static TaskEventEmitter createIOTask(TaskExecutor executor,String fileName) {
        final IOTask task = new IOTask(executor,fileName,"UTF-8");
        task.on("open", event -> {
            String fileName1 = (String)event.getArgs()[0];
            System.out.println(Thread.currentThread() + " - " + fileName1 + "has been open.");
        });

        task.on("next", event -> {
            BufferedReader reader = (BufferedReader)event.getArgs()[0];
            try {
                String line = reader.readLine();
                if(line != null) {
                    task.emit("ready",line);
                    task.emit("next",reader);
                } else {
                    task.emit("close",task.getFileName());
                }
            } catch (IOException e) {
                task.emit(e.getClass().getName(),e,task.getFileName());
                try{
                    reader.close();
                    task.emit("close",task.getFileName());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        task.on("ready", event -> {
            String line = (String) event.getArgs()[0];
            int len = line.length();
            int wordCount = line.split("[\\s+,.]+").length;
            System.out.println(Thread.currentThread() + " - word count: " + wordCount +"; length: " + len
            );
        });

        task.on("close",event -> {
            String fileName1 = (String) event.getArgs()[0];
            System.out.println(Thread.currentThread()+ " - " + fileName1 + "has been closed");
        });

        task.on(IOException.class.getName(), event -> {
            Object[] args = event.getArgs();
            IOException e = (IOException) args[0];
            String fileName1 = (String) args[1];
            System.out.println(Thread.currentThread() + " - An IOException occurred while reading " + fileName1 + ", error:" + e.getMessage());
        });

        task.on(FileNotFoundException.class.getName(), event -> {
            FileNotFoundException e = (FileNotFoundException)event.getArgs()[0];
            e.printStackTrace();
            System.exit(1);
        });

        return task;
    }

    public static TaskEventEmitter createPiTask(TaskExecutor executor,int n) {
        final PICalcTask task = new PICalcTask(executor,n);
        //计算下一个级数项
        task.on("next", event -> {
            int n1 = (Integer) event.getArgs()[0];
            double xn = Math.pow(-1, n1 -1)/(2* n1 -1);
            task.emit("sum",xn);
        });

        task.on("sum", new EventHandler() {
            private int i = 0;
            private double sum = 0;
            @Override
            public void handle(EventObject event) {
                double xn = ((Double)event.getArgs()[0]);
                sum += xn;
                i++;
//                System.out.println(Thread.currentThread()+ " - sum " + sum);
                if(i > task.getN()){
                    task.emit("finish",sum * 4);
                } else {
                    task.emit("next", i + 1);
                }
            }
        });

        task.on("finish",event -> {
            Double sum = (Double) event.getArgs()[0];
            System.out.println("pi = " + sum);
        });

        return task;
    }
}
