package com.sap.exercise.handler;

import com.sap.exercise.model.Event;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import java.util.Calendar;

public abstract class AbstractNotification implements Notification {

    private long timeTo;
    private Thread callingThread;
    protected Event event;

    AbstractNotification(Event event) {
        Calendar timeOf = event.getTimeOf();
        Calendar now = Calendar.getInstance();

        this.timeTo = event.getAllDay() ? 0 : (timeOf.getTimeInMillis() - now.getTimeInMillis())
                - (event.getReminder() * DateUtils.MILLIS_PER_MINUTE);
        if (this.timeTo < 0) {
            this.timeTo = 0;
        }

        this.event = event;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.timeTo);
            this.send();
        } catch (InterruptedException e) {
            Logger.getLogger(PopupNotification.class).debug("Notification deleted", e);
        }
    }

    @Override
    public void setCallingThread() {
        this.callingThread = Thread.currentThread();
    }

    @Override
    public void delete() {
        callingThread.interrupt();
    }
}
