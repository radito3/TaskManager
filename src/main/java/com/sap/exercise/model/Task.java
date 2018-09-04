package com.sap.exercise.model;

import com.sap.exercise.db.DatabaseUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "Task")
public class Task implements Serializable/*extends BaseEvent*/ {

    @Transient
    private AtomicInteger counter = new AtomicInteger(0);

    @Id
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->varchar(64)", name = "Title", nullable = false)
    private String title;

    @Column(columnDefinition = "mysql->text", name = "Body", nullable = false)
    private String body;


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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        // input filters
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        // input filters
        this.body = body;
    }

    public Task getTask() {
        return new DatabaseUtil().getObject(s -> s.get(Task.class, getId()));
    }

    public void create() {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.save(this));

        System.out.println("event created");
    }

    public void update(String... vars) {
        DatabaseUtil db = new DatabaseUtil();
        Task updated = new Task();
        updated.setId(this.getId());

        if (vars.length < 1 || vars.length > 2) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }

        updated.setTitle(vars[0]);

        if (vars.length == 2) {
            updated.setBody(vars[1]);
        }

        db.processObject(s -> s.update(updated));

        System.out.println("event updated");
    }

    public void delete() {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.delete(this));

        System.out.println("event deleted");
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ",title=" + getTitle() +
                ",body=" + getBody() +
                "}";
    }
}
