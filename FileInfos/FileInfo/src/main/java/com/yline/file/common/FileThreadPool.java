package com.yline.file.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 工程内的线程池
 *
 * @author yline 2018/2/8 -- 16:13
 * @version 1.0.0
 */
public class FileThreadPool {
    private static ExecutorService sFixedThreadPool;
    private static ExecutorService sSingleThreadPool;

    /**
     * 固定大小线程池，暂定5个
     */
    public static void fixedThreadExecutor(Runnable runnable) {
        if (sFixedThreadPool == null) {
            sFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        sFixedThreadPool.execute(runnable);
    }

    /**
     * 单线程池
     */
    public static void singleThreadExecutor(Runnable runnable) {
        if (sSingleThreadPool == null) {
            sSingleThreadPool = Executors.newSingleThreadExecutor();
        }
        sSingleThreadPool.execute(runnable);
    }

    public static ExecutorService getFixedThreadPool() {
        if (sFixedThreadPool == null) {
            sFixedThreadPool = Executors.newFixedThreadPool(5);
        }
        return sFixedThreadPool;
    }
}
