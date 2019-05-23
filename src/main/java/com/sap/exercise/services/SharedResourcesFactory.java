package com.sap.exercise.services;

import com.sap.exercise.notifications.NotificationFactory;

import java.util.concurrent.TimeUnit;

public class SharedResourcesFactory {

    private static AsyncExecutionsService asyncExecutionsService = new AsyncExecutionsService();
    private static EventsMapService eventsMapService;

    static {
        asyncExecutionsService.schedule(NotificationFactory::pollForNotifications,
                0L, 10L, TimeUnit.SECONDS);
    }

    public static AsyncExecutionsService getAsyncExecutionsService() {
        return asyncExecutionsService;
    }

    public static EventsMapService getEventsMapService() {
        if (eventsMapService == null) {
            eventsMapService = new EventsMapService();
        }
        return eventsMapService;
    }

    public static void close() {
        asyncExecutionsService.close();
        if (eventsMapService != null)
            eventsMapService.close();
    }
}