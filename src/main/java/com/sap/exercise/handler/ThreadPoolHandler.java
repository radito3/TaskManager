package com.sap.exercise.handler;

import org.apache.log4j.Logger;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolHandler implements Closeable {

    private ExecutorService service;

    ThreadPoolHandler() {
        service = Executors.newCachedThreadPool();
    }

    public void submit(Runnable runnable) {
        service.submit(runnable);
    }

    @Override
    public void close() {
        service.shutdown();
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Logger.getLogger(ThreadPoolHandler.class).error("Thread pool termination error", e);
        }
    }
}
