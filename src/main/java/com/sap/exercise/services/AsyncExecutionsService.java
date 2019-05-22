package com.sap.exercise.services;

import com.sap.exercise.notifications.NotificationFactory;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AsyncExecutionsService implements Closeable {

    private final ScheduledExecutorService scheduledService;

    AsyncExecutionsService() {
        scheduledService = Executors.newScheduledThreadPool(2);
    }

    void schedulePollingForNotifications() {
        scheduledService.scheduleAtFixedRate(NotificationFactory::pollForNotifications, 0L, 10L, TimeUnit.SECONDS);
    }

    public void execute(Runnable task) {
        scheduledService.execute(task);
    }

    @Override
    public void close() {
        scheduledService.shutdown();
    }
}