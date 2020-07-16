package com.sap.exercise.notifications;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class Notification implements Delayed {

    private Date date;

    protected Notification(Date date) {
        this.date = date;
    }

    public abstract void send();

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(System.currentTimeMillis() - date.getTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof Notification) {
            return date.compareTo(((Notification) o).date);
        }
        return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
    }
}
