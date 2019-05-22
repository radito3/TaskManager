package com.sap.exercise.services;

public class SharedResourcesFactory {

    private static AsyncExecutionsService asyncExecutionsService = new AsyncExecutionsService();
    private static EventsMapService eventsMapService;

    static {
        asyncExecutionsService.schedulePollingForNotifications();
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