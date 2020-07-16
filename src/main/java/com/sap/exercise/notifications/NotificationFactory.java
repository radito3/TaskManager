package com.sap.exercise.notifications;

import com.sap.exercise.Configuration;
import com.sap.exercise.model.Event;

import java.time.LocalDateTime;

public class NotificationFactory {

    private NotificationFactory() {
    }

    public static Notification newNotification(Event event, LocalDateTime time) {
        switch (Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotification(event, time);
            case EMAIL:
                return new EmailNotification(event, time);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }
}
