package com.sap.exercise.handler;

public class SharedResourcesFactory {

    private static AsyncExecutionsService service;
    private static EventsMapHandler mapHandler;

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
