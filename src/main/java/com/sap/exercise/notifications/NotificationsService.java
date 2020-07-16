package com.sap.exercise.notifications;

import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateParser;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

//ideally this would be a singleton bean
public class NotificationsService {

    private static BlockingQueue<Notification> queue = new DelayQueue<>();

    public static void pollForUpcomingEvents() {
        DateParser today = new DateParser();
        Collection<Event> todayEvents = new EventDao()
                    //FIXME This condition needs to be changed so the sql is
                    // SELECT * FROM Eventt WHERE Date(TimeOf) = <today>
            .getAll(new TimeFrameCondition(today.asString(), today.asString()));

        for (Event event : todayEvents) {
            long timeToNotify;
            if (event.getAllDay()) {
                timeToNotify = System.currentTimeMillis();
            } else {
                timeToNotify = event.getTimeOf().getTimeInMillis() - TimeUnit.MINUTES.toMillis(event.getReminder());
            }

            queue.add(NotificationFactory.newNotification(event, new Date(timeToNotify)));
        }

        scheduleNotifications();
    }

    private static void scheduleNotifications() {
        Thread t = new Thread(() -> {
            while (!queue.isEmpty()) {
                try {
                    queue.take().send();
                } catch (InterruptedException e) {
                    System.err.println("Suppressed notification");
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
