package com.sap.exercise.handler;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import com.sap.exercise.Application;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;

//this abstract class already too many responsibilities. see below. 
//Btw, i guess this is why the naming of this class is so ambiguous - inheritors are 'handlers' but this one - not :)
public abstract class Notifications {

    // it maintains a *statically referenced* cache of notifications
    protected static final Map<Serializable, Thread> eventNotifications = new Hashtable<>();
    // btw, do all notification handlers have to use that printer, via a static reference?
    protected static final OutputPrinter printer = new OutputPrinter(Application.Configuration.OUTPUT);

    // it gives some standardized state to inheriting notification handlers but defines no such for behavior?
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

    // it is a factory for notification handlers, and has to effectively directly reference it's inheritors
    static Runnable newNotificationHandler(Event event) {
        switch (Application.Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotificationHandler(event);
            case EMAIL:
                return new EmailNotificationHandler(event);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }

    // it's a factory for this weird irrelevant runnable
    public static Runnable onDelete(Event event) {
        return () -> {
            removeEvent(event);
        };
    }

    public static void removeEvent(Event event) {
        Thread thread;
        if ((thread = eventNotifications.get(event.getId())) != null) {
            thread.interrupt();
            eventNotifications.remove(event.getId());
        }
    }
}
