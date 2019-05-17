package com.sap.exercise.handler;

public class SharedResourcesFactory {

    private static AsyncExecutionsService asyncExecutionsService = new AsyncExecutionsService();
    private static EventsMapHandler eventsMapHandler;

    static {
        asyncExecutionsService.schedulePollingForNotifications();
    }

    public static AsyncExecutionsService getAsyncExecutionsService() {
        return asyncExecutionsService;
    }

    public static EventsMapHandler getEventsMapHandler() {
        if (eventsMapHandler == null) {
            eventsMapHandler = new EventsMapHandler();
        }
        return eventsMapHandler;
    }

    public static void shutdown() {
        asyncExecutionsService.close();
        if (eventsMapHandler != null)
            eventsMapHandler.close();
    }
}