package com.sap.exercise.notifications;

import com.sap.exercise.Configuration;
import com.sap.exercise.model.Event;

import java.util.Date;

public class NotificationFactory {

    private NotificationFactory() {
    }

    public static Notification newNotification(Event event, Date date) {
        switch (Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotification(event, date);
            case EMAIL:
                return new EmailNotification(event, date);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }
}
