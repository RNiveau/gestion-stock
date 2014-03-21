package net.blog.dev.gestion.stocks.back.callback;

/**
 * Created by romainn on 18/03/2014.
 */
public interface ICallback<T> {

    public void complete(T result);
}

