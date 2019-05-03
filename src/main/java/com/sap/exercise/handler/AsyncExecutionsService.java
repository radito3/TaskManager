package com.sap.exercise.handler;

import com.sap.exercise.db.DatabaseClientHolder;
import com.sap.exercise.model.Event;
import com.sap.exercise.notifications.NotificationFactory;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Closeable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AsyncExecutionsService implements Closeable {

    private final ScheduledExecutorService se = Executors.newScheduledThreadPool(2);
    private final Set<Event> sentNotificationsEvents = new HashSet<>();

    AsyncExecutionsService() {
        se.execute(DatabaseClientHolder::createDbClient);
        se.scheduleAtFixedRate(this::pollForNotifications, 0L,10L, TimeUnit.SECONDS);
    }

    public void execute(Runnable task) {
        se.execute(task);
    }

    private synchronized void pollForNotifications() {
        DateHandler today = new DateHandler(DateHandler.Dates.TODAY);
        Set<Event> todayEvents = new EventGetter().getEventsInTimeFrame(today.asString(), today.asString());

        todayEvents.forEach(event -> {
            Calendar timeOf = event.getTimeOf();
            Calendar now = Calendar.getInstance();

            long time = (timeOf.getTimeInMillis() - now.getTimeInMillis())
                    - (event.getReminder() * DateUtils.MILLIS_PER_MINUTE);

            if ((event.getAllDay() || time <= 0) && !sentNotificationsEvents.contains(event)) {
                NotificationFactory.newNotification(event).send();
                sentNotificationsEvents.add(event);
            }
        });
    }

    @Override
    public void close() {
        se.shutdown();
    }
}
