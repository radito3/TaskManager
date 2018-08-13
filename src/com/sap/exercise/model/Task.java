package com.sap.exercise.model;

public class Task extends BaseEvent implements EventActions {

    public Task() {
        setBody("task body");
    }

    public Task(String text) {
        setBody(text);
    }

    @Override
    public void create() {
        // database entry creation
        System.out.println("event created");
    }

    @Override
    public void delete() {
        // database entry deletion
        System.out.println("event deleted");
    }
}
