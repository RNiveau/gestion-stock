package net.blog.dev.gestion.stocks.back.callback;

/**
 * Created by romainn on 18/03/2014.
 */
public class CallBackTask implements Runnable {

    private final IRunnableTask task;

    private final ICallback callBack;

    public CallBackTask(IRunnableTask task, ICallback callBack) {
        this.task = task;
        this.callBack = callBack;
    }

    public void run() {
        task.run();
        callBack.complete(task.getResult());
    }
}
