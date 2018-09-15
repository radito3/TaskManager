package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Reminder")
public class Reminder extends BaseEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->varchar(128)", name = "Title", nullable = false)
    private String title;

    public Reminder() {
        this("reminder title");
    }

    public Reminder(String title) {
        setTitle(title);
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
//        filter(title, str -> str.matches("[-_.a-zA-Z0-9]"), IllegalArgumentException::new);
        this.title = title;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", title='" + title + '\'' +
                "}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Reminder reminder = (Reminder) object;
        return Objects.equals(id, reminder.id) &&
                Objects.equals(title, reminder.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
