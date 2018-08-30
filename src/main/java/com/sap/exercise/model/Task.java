package com.sap.exercise.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "Task")
public class Task implements Serializable/*extends BaseEvent*/ {

//    private AtomicInteger counter = new AtomicInteger(0);

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
        setId(new AtomicInteger().getAndIncrement());
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ",title=" + getTitle() +
                ",body=" + getBody() +
                "}";
    }
}
