package com.sap.exercise.model;

import com.sap.exercise.db.DatabaseUtil;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Task")
public class Task extends BaseEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        return new DatabaseUtil().getObject(s -> s.get(Task.class, this.getId()));
    }

    public void create() {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.save(this));
    }

    public void update(String... vars) {
        DatabaseUtil db = new DatabaseUtil();

        if (vars.length < 1 || vars.length > 2) {
            throw new IllegalArgumentException("Wrong amount of arguments");
        }

        this.setTitle(vars[0]);

        if (vars.length == 2) {
            this.setBody(vars[1]);
        }

        db.processObject(s -> s.update(this));
    }

    public void delete() {
        DatabaseUtil db = new DatabaseUtil();

        db.processObject(s -> s.delete(this));
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
