package com.sap.exercise.notifications;

import com.sap.exercise.commands.ExitCommand;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateParser;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;

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
            LocalDateTime timeToNotify;

            if (event.getAllDay()) {
                timeToNotify = LocalDateTime.now();
            } else {
                timeToNotify = event.getTimeOf()
                                    .minusMinutes(event.getReminder());
            }

            queue.add(NotificationFactory.newNotification(event, timeToNotify));
        }

        scheduleNotifications();
    }

    private static void scheduleNotifications() {
        Thread t = new Thread(() -> {
            while (!queue.isEmpty()) {
                try {
                    queue.take().send();
                } catch (InterruptedException e) {
                    Logger.getLogger(NotificationsService.class).error("Suppressed notification");
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
