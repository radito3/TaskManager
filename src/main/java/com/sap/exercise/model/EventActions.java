package com.sap.exercise.model;

public interface EventActions {
    // database modification actions

    <T extends BaseEvent> void create(T obj);

    <T extends BaseEvent> void update(T obj);

    <T extends BaseEvent> void delete(T obj);
}
