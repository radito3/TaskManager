package com.sap.exercise.services;

public class SharedResourcesFactory {

    private static AsyncExecutionsService asyncExecutionsService;
    private static EventsCache eventsCache;

    public static AsyncExecutionsService getAsyncExecutionsService() {
        if (asyncExecutionsService == null)
            asyncExecutionsService = new AsyncExecutionsService();
        return asyncExecutionsService;
    }

    public static EventsCache getEventsCache() {
        if (eventsCache == null)
            eventsCache = new EventsCache();
        return eventsCache;
    }

    public static void shutdown() {
        if (asyncExecutionsService != null)
            asyncExecutionsService.close();
        if (eventsCache != null)
            eventsCache.close();
    }
}