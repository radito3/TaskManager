package com.sap.exercise.handler;

public class SharedResourcesFactory {

    private static AsyncExecutionsService service = new AsyncExecutionsService();
    private static EventsMapHandler mapHandler = new EventsMapHandler();

    public static AsyncExecutionsService getService() {
        if (service == null) {
            service = new AsyncExecutionsService();
        }
        return service;
    }

    public static EventsMapHandler getMapHandler() {
        if (mapHandler == null) {
            mapHandler = new EventsMapHandler();
        }
        return mapHandler;
    }

    public static void shutdown() {
        service.close();
        mapHandler.close();
    }
}
