package com.sap.exercise.handler;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Notifications {

    private static Map<Serializable, Thread> events = new ConcurrentHashMap<>();

    static void put(Serializable id, Thread thread) {
        events.put(id, thread);
    }

    static boolean contains(Serializable id) {
        return events.containsKey(id);
    }

    static Thread get(Serializable id) {
        return events.get(id);
    }

    static void delete(Serializable id) {
        events.remove(id);
    }
}
