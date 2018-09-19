package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Eventt")
public class Event extends BaseEvent implements Serializable {

    private enum EventType {
        TASK, REMINDER, GOAL
    }

    private enum RepeatableType {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->varchar(64)", name = "Title", nullable = false)
    private String title;

    @Column(columnDefinition = "mysql->enum('Task', 'Reminder', 'Goal')", name = "TypeOf", nullable = false)
    private EventType typeOf;

    @Column(columnDefinition = "mysql->text", name = "Location")
    private String location;

    @Column(columnDefinition = "mysql->timestamp", name = "TimeOf")
    private Timestamp timeOf;

    @Column(columnDefinition = "mysql->text", name = "Description")
    private String description;

    @Column(columnDefinition = "mysql->tinyint(1)", name = "AllDay", nullable = false)
    private Boolean allDay;

    @Column(columnDefinition = "mysql->time", name = "Duration", nullable = false)
    private Time duration;

    @Column(columnDefinition = "mysql->int(11)", name = "Reminder", nullable = false)
    private Integer reminder;

    @Column(columnDefinition = "mysql->enum('Daily', 'Weekly', 'Monthly', 'Yearly')", name = "ToRepeat")
    private RepeatableType toRepeat;

    public Event() {
//        this("task title", "task body");
    }
//
//    public Event(String title) {
//        this(title, "task body");
//    }
//
//    public Event(String title, String body) {
//        this(title, body, false, Time.valueOf(LocalTime.MIN));
//    }
//
//    public Event(String title, String body, Boolean allDay, Time duration) {
//        setTitle(title);
//        setBody(body);
//        setAllDay(allDay);
//        setDuration(duration);
//    }

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
        this.title = title;
    }

    public EventType getTypeOf() {
        return typeOf;
    }

    public void setTypeOf(EventType typeOf) {
        this.typeOf = typeOf;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getTimeOf() {
        return timeOf;
    }

    public void setTimeOf(Timestamp timeOf) {
        this.timeOf = timeOf;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getReminder() {
        return reminder;
    }

    public void setReminder(Integer reminder) {
        this.reminder = reminder;
    }

    public RepeatableType getToRepeat() {
        return toRepeat;
    }

    public void setToRepeat(RepeatableType toRepeat) {
        this.toRepeat = toRepeat;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", typeOf=" + typeOf +
                ", location='" + location + '\'' +
                ", timeOf=" + timeOf +
                ", description='" + description + '\'' +
                ", allDay=" + allDay +
                ", duration=" + duration +
                ", reminder=" + reminder +
                ", toRepeat=" + toRepeat +
                "}";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Event event = (Event) object;
        return Objects.equals(id, event.id) &&
                Objects.equals(title, event.title) &&
                typeOf == event.typeOf &&
                Objects.equals(location, event.location) &&
                Objects.equals(timeOf, event.timeOf) &&
                Objects.equals(description, event.description) &&
                Objects.equals(allDay, event.allDay) &&
                Objects.equals(duration, event.duration) &&
                Objects.equals(reminder, event.reminder) &&
                toRepeat == event.toRepeat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, typeOf, location, timeOf, description, allDay, duration, reminder, toRepeat);
    }

//        filter(duration, time -> time.toLocalTime().isAfter(LocalTime.MAX)
//                || time.toLocalTime().isBefore(LocalTime.MIN), IllegalArgumentException::new);

}
