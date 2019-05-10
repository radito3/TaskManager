package com.sap.exercise.handler;

import com.sap.exercise.model.Event;
import com.sap.exercise.notifications.NotificationFactory;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.lang3.time.DateUtils;

import java.io.Closeable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AsyncExecutionsService implements Closeable {

    private final ScheduledExecutorService se = Executors.newScheduledThreadPool(2);
    private final Set<Integer> sentNotificationsEvents = new HashSet<>();

    AsyncExecutionsService() {
        se.scheduleAtFixedRate(this::pollForNotifications, 0L, 10L, TimeUnit.SECONDS); // Do you think this should be started inside this
                                                                                       // 'Service'? I'd assume the service would be responsible
                                                                                       // to take input for what to execute, not start some tasks
                                                                                       // on its own. Perhaps something else should be responsible for starting this repetitive 'execution'
    }

    public void execute(Runnable task) {
        se.execute(task);
    }


    private synchronized void pollForNotifications() {
        DateHandler today = new DateHandler(DateHandler.Dates.TODAY);
        Set<Event> todayEvents = new EventGetter().getEventsInTimeFrame(today.asString(), today.asString());

        todayEvents.forEach(event -> {
            long time = (event.getTimeOf().getTimeInMillis() - today.asCalendar().getTimeInMillis())
                    - (event.getReminder() * DateUtils.MILLIS_PER_MINUTE);

            if ((event.getAllDay() || time <= 0) && !sentNotificationsEvents.contains(event.getId())) {
                NotificationFactory.newNotification(event).send();
                sentNotificationsEvents.add(event.getId());
            }
        });
    }

    @Override
    public void close() {
        se.shutdown();
        sentNotificationsEvents.clear();
    }
}