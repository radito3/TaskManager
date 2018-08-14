package com.sap.exercise.model;

import com.sap.exercise.Main;

import java.util.concurrent.atomic.AtomicInteger;

public class Task extends BaseEvent implements EventActions<Task> {

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
        create(this);
    }


    // not sure if db modification methods should be in this class
    @Override
    public void create(Task obj) {
        // database entry creation
        /* temporary */
        Main.addTask(obj);

        System.out.println("event created");
    }

    @Override
    public void update(Task obj) {
        // database entry updating
        /* temporary */
        this.setTitle(obj.getTitle());
        this.setBody(obj.getBody());

        System.out.println("event updated");
    }

    @Override
    public void delete(Task obj) {
        // database entry deletion
        /* temporary */
        Main.getTasks().remove(obj);

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
