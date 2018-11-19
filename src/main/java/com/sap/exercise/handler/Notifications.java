package com.sap.exercise.handler;

import com.sap.exercise.Application;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public abstract class Notifications {

    protected static final Map<Serializable, Thread> eventNotifications = new Hashtable<>();

    protected static final OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    protected long timeTo;
    protected Event event;

    Notifications(Event event) {
        if (!eventNotifications.containsKey(event.getId())) {
            Calendar timeOf = event.getTimeOf();
            Calendar now = Calendar.getInstance();

            this.timeTo = event.getAllDay() ? 0 : (timeOf.getTimeInMillis() - now.getTimeInMillis())
                    - (event.getReminder() * DateUtils.MILLIS_PER_MINUTE);
            this.event = event;

            eventNotifications.put(event.getId(), Thread.currentThread());
        }
    }

    static Runnable newNotificationHandler(Event event) {
        switch (Application.Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotificationHandler(event);
            case EMAIL:
                return new EmailNotificationHandler(event);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }

    static Runnable onDelete(Event event) {
        return () -> {
            Thread thread;
            if ((thread = eventNotifications.get(event.getId())) != null) {
                thread.interrupt();
                eventNotifications.remove(event.getId());
            }
        };
    }
}
