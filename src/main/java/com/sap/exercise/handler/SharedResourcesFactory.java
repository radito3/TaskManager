package com.sap.exercise.handler;

public class SharedResourcesFactory {

    private static AsyncExecutionsService service = new AsyncExecutionsService();
    private static EventsMapHandler eventsMapHandler = new EventsMapHandler(); // Do not be afraid to make descriptive variable names

    public static AsyncExecutionsService getService() {
        if (service == null) { // Will this path ever be taken? You have already initialized it. Choose one of
                               // both. Which one would you consider better - the eager initialization on class
                               // loading or lazy initialization on first usage?
            service = new AsyncExecutionsService();
        }
        return service;
    }

    public static EventsMapHandler getMapHandler() {
        if (eventsMapHandler == null) { // Will this path ever be taken? You have already initialized it. Choose one of
                                  // both. Which one would you consider better - the eager initialization on class
                                  // loading or lazy initialization on first usage?
            eventsMapHandler = new EventsMapHandler();
        }
        return eventsMapHandler;
    }

    public static void shutdown() {
        service.close();
        eventsMapHandler.close();
    }
}