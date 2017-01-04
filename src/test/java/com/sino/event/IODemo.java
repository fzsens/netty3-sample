package com.sino.event;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by thierry.fu on 2017/1/3.
 */
public class IODemo {

    public static void main(String[] args) throws InterruptedException {
        String fileName = "C:/info.txt";
        final TaskManager manager = new TaskManager();
        manager.start();
        TaskExecutor executor = manager.getExecutor();
        Task ioTask = TaskHelper.createIOTask(executor,fileName);
        executor.submit(ioTask);
        TimeUnit.SECONDS.sleep(50);
        manager.stop();
    }
}

class IOTask extends TaskEventEmitter {

    private final String fileName;

    private final String encoding;

    public IOTask(TaskExecutor executor,String fileName,String encoding) {
        super(executor);
        this.fileName = fileName;
        this.encoding = encoding;
    }

    public String getFileName() {
        return fileName;
    }

    public String getEncoding() {
        return encoding;
    }

    @Override
    protected void run() throws Exception {
        InputStream fis = new FileInputStream(new File(fileName));
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis,getEncoding()));
        emit("open",getFileName());
        emit("next",reader);
    }
}
