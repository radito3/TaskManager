package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseClientHolder;

import java.io.Closeable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class PollingService implements Closeable {

    private final ScheduledExecutorService se = Executors.newScheduledThreadPool(2);

    PollingService() {
        se.execute(DatabaseClientHolder::createDbClient);
        se.scheduleAtFixedRate(() -> {
                    //TODO implement polling for notifications
                },
                0L,
                10L,
                TimeUnit.SECONDS);
    }

    public void execute(Runnable task) {
        se.execute(task);
    }

    @Override
    public void close() {
        se.shutdown();
    }
}
