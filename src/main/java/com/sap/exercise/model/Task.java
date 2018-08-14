package com.sap.exercise.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Task extends BaseEvent implements EventActions {

    private AtomicInteger counter = new AtomicInteger(0);

    public Task() {
        this("task title", "task body");
    }

    public Task(String title) {
        this(title, "task body");
    }

    public Task(String title, String body) {
        setId(counter.getAndIncrement());
        setTitle(title);
        setBody(body);
    }

    @Override
    public <Task extends BaseEvent> void create(Task obj) {
        // database entry creation
        System.out.println("event created");
    }

    @Override
    public <Task extends BaseEvent> void update(Task obj) {
        // database entry updating
        setBody(obj.getBody());
        System.out.println("event updated");
    }

    @Override
    public <Task extends BaseEvent> void delete(Task obj) {
        // database entry deletion
        System.out.println("event deleted");
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + counter +
                ",title=" + getTitle() +
                ",body=" + getBody() +
                "}";
    }
}
