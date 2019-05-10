package com.sap.exercise.handler;

public class SharedResourcesFactory {

    private static AsyncExecutionsService service = new AsyncExecutionsService();
    private static EventsMapHandler eventsMapHandler;

    static {
        service.schedulePollingForNotifications();
    }

    public static AsyncExecutionsService getService() {
        return service;
    }

    public static EventsMapHandler getMapHandler() {
        if (eventsMapHandler == null) {
            eventsMapHandler = new EventsMapHandler();
        }
        return eventsMapHandler;
    }

    public static void shutdown() {
        service.close();
        if (eventsMapHandler != null)
            eventsMapHandler.close();
    }
}