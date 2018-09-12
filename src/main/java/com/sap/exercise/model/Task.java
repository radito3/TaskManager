package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;

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

    @Column(columnDefinition = "mysql->tinyint(1)", name = "AllDay", nullable = false)
    private Boolean allDay;

    @Column(columnDefinition = "mysql->time", name = "Duration", nullable = false)
    private Time duration;

    public Task() {
        this("task title", "task body");
    }

    public Task(String title) {
        this(title, "task body");
    }

    public Task(String title, String body) {
        this(title, body, false, Time.valueOf(LocalTime.MIN));
    }

    public Task(String title, String body, Boolean allDay, Time duration) {
        setTitle(title);
        setBody(body);
        setAllDay(allDay);
        setDuration(duration);
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

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ",title=" + title +
                ",body=" + body +
                ",allDay=" + allDay +
                ",duration=" + duration +
                "}";
    }
}
