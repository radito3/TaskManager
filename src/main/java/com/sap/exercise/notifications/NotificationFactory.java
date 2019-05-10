package com.sap.exercise.notifications;

import com.sap.exercise.Application;
import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.SharedResourcesFactory;
import com.sap.exercise.model.Event;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class NotificationFactory {

    private static Set<Integer> sentNotificationsEvents = Collections.synchronizedSet(new HashSet<>());

    private static Notification newNotification(Event event) {
        switch (Application.Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotification(event);
            case EMAIL:
                return new EmailNotification(event);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }

    public static void pollForNotifications() {
        DateHandler today = new DateHandler(DateHandler.Dates.TODAY);
        Set<Event> todayEvents = new EventGetter().getEventsInTimeFrame(today.asString(), today.asString());

        todayEvents.forEach(event -> SharedResourcesFactory.getService().execute(() -> {
            long time = (event.getTimeOf().getTimeInMillis() - today.asCalendar().getTimeInMillis())
                    - (event.getReminder() * DateUtils.MILLIS_PER_MINUTE);

            if ((event.getAllDay() || time <= 0) && !sentNotificationsEvents.contains(event.getId())) {
                NotificationFactory.newNotification(event).send();
                sentNotificationsEvents.add(event.getId());
            }
        }));
    }
}
