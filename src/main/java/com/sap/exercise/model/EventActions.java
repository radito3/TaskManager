package com.sap.exercise.model;

public interface EventActions<T extends BaseEvent> {
    // database modification actions

    void create(T obj);

    void update(T obj);

    void delete(T obj);
}
