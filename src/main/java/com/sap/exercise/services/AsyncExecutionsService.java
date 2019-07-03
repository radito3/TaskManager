package com.sap.exercise.services;

import java.io.Closeable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class AsyncExecutionsService implements Closeable {

    private final ScheduledThreadPoolExecutor scheduledExecutor;

    AsyncExecutionsService() {
        scheduledExecutor = new ScheduledThreadPoolExecutor(2, new ThreadPoolExecutor.DiscardOldestPolicy());
        scheduledExecutor.setMaximumPoolSize(5);
    }

    public void schedule(Runnable task, long initial, long delay, TimeUnit timeUnit) {
        scheduledExecutor.scheduleAtFixedRate(task, initial, delay, timeUnit);
    }

    public void execute(Runnable task) {
        scheduledExecutor.execute(task);
    }

    @Override
    public void close() {
        scheduledExecutor.shutdown();
    }
}