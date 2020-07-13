package com.sap.exercise.services;

public class SharedResourcesFactory {

    private static AsyncExecutionsService asyncExecutionsService;
    private static EventsMapService eventsMapService;

    public static AsyncExecutionsService getAsyncExecutionsService() {
        if (asyncExecutionsService == null)
            asyncExecutionsService = new AsyncExecutionsService();
        return asyncExecutionsService;
    }

    public static EventsMapService getEventsMapService() {
        if (eventsMapService == null)
            eventsMapService = new EventsMapService();
        return eventsMapService;
    }

    public static void shutdown() {
        if (asyncExecutionsService != null)
            asyncExecutionsService.close();
        if (eventsMapService != null)
            eventsMapService.close();
    }
}