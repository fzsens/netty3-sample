package com.sino.event;

/**
 * Created by thierry.fu on 2017/1/4.
 */
public class PICalcDemo {
    public static void main(String[] args) {
        final TaskManager manager = new TaskManager();
        manager.start();
        TaskExecutor executor = manager.getExecutor();
        TaskEventEmitter piTask = TaskHelper.createPiTask(executor,500000);
        EventHandler handler = event -> manager.stop();
        executor.submit(piTask);
        piTask.on("finish",handler);
    }

}

class PICalcTask extends TaskEventEmitter {

    private final int N;

    public PICalcTask(TaskExecutor executor, int n) {
        super(executor);
        this.N = n;
    }

    public int getN(){
        return N;
    }

    @Override
    protected void run() throws Exception {
        emit("next",1);
    }
}
