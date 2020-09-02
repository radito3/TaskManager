package com.sap.exercise.notifications;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Notification implements Delayed {

    private final LocalDateTime time;

    protected Notification(LocalDateTime time) {
        this.time = time;
    }

    public abstract void send();

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(LocalDateTime.now().until(time, ChronoUnit.MILLIS), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof Notification) {
            return time.compareTo(((Notification) o).time);
        }
        return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }
}
