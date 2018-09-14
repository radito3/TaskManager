package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;

@Entity
@Table(name = "Goal")
public class Goal extends BaseEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->varchar(64)", name = "Title", nullable = false)
    private String title;

    @Column(columnDefinition = "mysql->time", name = "Duration", nullable = false)
    private Time duration;

    public Goal() {
        this("goal title");
    }

    public Goal(String title) {
        this(title, Time.valueOf(LocalTime.MIN));
    }

    public Goal(String title, Time duration) {
        setTitle(title);
        setDuration(duration);
    }

    @Override
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
        //input filter
        this.title = title;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        //input filter
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                "}";
    }
}
