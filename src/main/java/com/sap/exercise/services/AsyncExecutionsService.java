package com.sap.exercise.services;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AsyncExecutionsService implements Closeable {

    private final ScheduledExecutorService scheduledService;

    AsyncExecutionsService() {
        scheduledService = Executors.newScheduledThreadPool(2);
    }

    public void schedule(Runnable task, long initial, long delay, TimeUnit timeUnit) {
        scheduledService.scheduleAtFixedRate(task, initial, delay, timeUnit);
    }

    public void execute(Runnable task) {
        scheduledService.execute(task);
    }

    @Override
    public void close() {
        scheduledService.shutdown();
    }
}