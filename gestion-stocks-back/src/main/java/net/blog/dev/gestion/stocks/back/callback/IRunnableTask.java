package net.blog.dev.gestion.stocks.back.callback;

/**
 * Created by romainn on 18/03/2014.
 */
public interface IRunnableTask<T> extends Runnable {

    public T getResult();

}
