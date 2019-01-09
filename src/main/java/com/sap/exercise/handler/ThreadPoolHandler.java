package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseClientHolder;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolHandler implements Closeable {

    private ExecutorService service;

    public ThreadPoolHandler() {
        service = Executors.newCachedThreadPool();
        service.submit(DatabaseClientHolder::createDbClient);
    }

    public void submit(Runnable runnable) {
        service.submit(runnable);
    }

    @Override
    public void close() {
        service.shutdown();
    }
}
