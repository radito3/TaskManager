package com.sap.exercise.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    @Mandatory
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
    @Mandatory
    private Boolean allDay;

    @Column(columnDefinition = "mysql->int(11)", name = "Duration", nullable = false)
    private Integer duration; //if allDay is true -> number of days; else -> number of minutes per day

    @Column(columnDefinition = "mysql->int(11)", name = "Reminder", nullable = false)
    private Integer reminder; //minutes before appointed time to remind

    @Column(columnDefinition = "mysql->enum('NONE', 'DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY')", name = "ToRepeat", nullable = false)
    @Enumerated(EnumType.STRING)
    @Alias("repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early")
    @Mandatory
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
        this(title, type, "", timeOf, "", false, 0, 0, repeat);
    }

    // Nice reuse of constructor. See Builder & Factory design patterns - would spare the pain of giving *9* arguments to a constructor
    public Event(String title, EventType typeOf, String location, Calendar timeOf, String description,
                 Boolean allDay, Integer duration, Integer reminder, RepeatableType toRepeat) {
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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

    // cool - spill your guts in a structured way :D
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", typeOf=" + typeOf +
                ", location='" + location + '\'' +
                ", timeOf=" + timeOf.toInstant().toString() +
                ", description='" + description + '\'' +
                ", allDay=" + allDay +
                ", duration=" + duration +
                ", reminder=" + reminder +
                ", toRepeat=" + toRepeat +
                "}";
    }

    // Nice implementation :) You can also check out org.apache.commons...EqualsBuilder - sorry for misleading you on the phone for the name
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

    // btw - this implementation is nice, you can also check-out apache.commons HashCodeBuilder - it's API is interesting (method chaining)
    @Override
    public int hashCode() {
        return Objects.hash(id, title, typeOf, location, timeOf, description, allDay, duration, reminder, toRepeat);
    }
}
