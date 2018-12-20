package com.sap.exercise.handler;

import com.sap.exercise.Application;
import com.sap.exercise.model.Event;

public class NotificationFactory {

    public static Notification newNotification(Event event) {
        switch (Application.Configuration.NOTIFICATION_TYPE) {
            case POPUP:
                return new PopupNotification(event);
            case EMAIL:
                return new EmailNotification(event);
        }
        throw new UnsupportedOperationException("Invalid notification type");
    }
}
