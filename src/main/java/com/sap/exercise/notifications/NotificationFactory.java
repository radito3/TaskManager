package com.sap.exercise.notifications;

import com.sap.exercise.Configuration;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.services.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NotificationFactory {

    private static Set<Integer> sentNotificationsEvents = Collections.synchronizedSet(new HashSet<>());

    private static Notification newNotification(Event event) {
        switch (Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotification(event);
            case EMAIL:
                return new EmailNotification(event);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }

    public static void pollForNotifications() {
        DateHandler today = new DateHandler();
        Collection<Event> todayEvents = new EventDao()
                .getAll(new TimeFrameCondition(today.toString(), today.toString()));

        for (Event event : todayEvents) {
            SharedResourcesFactory.getAsyncExecutionsService().execute(() -> {
                long time = (event.getTimeOf().getTimeInMillis() - today.asCalendar().getTimeInMillis())
                        - (event.getReminder() * DateUtils.MILLIS_PER_MINUTE);

                if ((event.getAllDay() || time <= 0) && !sentNotificationsEvents.contains(event.getId())) {
                    newNotification(event).send();
                    sentNotificationsEvents.add(event.getId());
                }
            });
        }
    }

    public static void clearEventsSet() {
        sentNotificationsEvents.clear();
    }
}
