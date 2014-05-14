package net.blog.dev.gestion.stocks.jfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by romainn on 14/05/2014.
 */
public class PoolThreadManager {

    private static Logger logger = LoggerFactory.getLogger(PoolThreadManager.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static ExecutorService getPoolThread() {
        return executorService;
    }

    public static void killPoolThread() {
        logger.debug("Shutdow poolThread");
        executorService.shutdownNow();
    }
}
