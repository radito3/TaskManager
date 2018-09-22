package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table(name = "Eventt")
public class Event extends BaseEvent implements Serializable {

    public enum EventType {
        TASK, REMINDER, GOAL
    }

    public enum RepeatableType {
        NONE, DAILY, WEEKLY, MONTHLY, YEARLY
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->varchar(64)", name = "Title", nullable = false)
    private String title;

    @Column(columnDefinition = "mysql->enum('TASK', 'REMINDER', 'GOAL')", name = "TypeOf", nullable = false)
    @Enumerated(EnumType.STRING)
    @Alias("type")
    private EventType typeOf;

    @Column(columnDefinition = "mysql->text", name = "Location")
    private String location;

    @Column(columnDefinition = "mysql->timestamp", name = "TimeOf")
    @Temporal(TemporalType.TIMESTAMP)
    @Alias("when")
    private Calendar timeOf;

    @Column(columnDefinition = "mysql->text", name = "Description")
    private String description;

    @Column(columnDefinition = "mysql->tinyint(1)", name = "AllDay", nullable = false)
    @Alias("all day? [Y]es [N]o")
    private Boolean allDay;

    //will input an integer and convert it to time remaining to the end
    @Column(columnDefinition = "mysql->timestamp", name = "Duration", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar duration; //this will be set to the end time

    //task only
    @Column(columnDefinition = "mysql->int(11)", name = "Reminder", nullable = false)
    private Integer reminder; //minutes before appointed time to remind

    @Column(columnDefinition = "mysql->enum('NONE', 'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY')", name = "ToRepeat", nullable = false)
    @Enumerated(EnumType.STRING)
    @Alias("repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early")
    private RepeatableType toRepeat;

    public Event() {
        this("default title");
    }

    public Event(String title) {
        this(title, EventType.TASK);
    }

    public Event(String title, EventType type) {
        this(title, type, Calendar.getInstance(), RepeatableType.NONE);
    }

    public Event(String title, EventType type, Calendar timeOf, RepeatableType repeat) {
        this(title, type, "", timeOf, "", false, Calendar.getInstance(), 0, repeat);
    }

    public Event(String title, EventType typeOf, String location, Calendar timeOf, String description,
                 Boolean allDay, Calendar duration, Integer reminder, RepeatableType toRepeat) {
        this.title = title;
        this.typeOf = typeOf;
        this.location = location;
        this.timeOf = timeOf;
        this.description = description;
        this.allDay = allDay;
        this.duration = duration;
        this.reminder = reminder;
        this.toRepeat = toRepeat;
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

    public Calendar getTimeOf() {
        return timeOf;
    }

    public void setTimeOf(Calendar timeOf) {
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

    public Calendar getDuration() {
        return duration;
    }

    public void setDuration(Calendar duration) {
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
                ", timeOf=" + timeOf.getTime().toString() +
                ", description='" + description + '\'' +
                ", allDay=" + allDay +
                ", duration=" + duration.getTime().toString() +
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
}
