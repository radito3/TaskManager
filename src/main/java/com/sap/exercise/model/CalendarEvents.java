package com.sap.exercise.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "CalendarEvents")
public class CalendarEvents implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "int(11)", name = "EventId", nullable = false)
    private Integer eventId;

    @Column(columnDefinition = "date", name = "Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate date;

    public CalendarEvents() {
    }

    public CalendarEvents(Integer eventId, LocalDate date) {
        this.eventId = eventId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarEvents that = (CalendarEvents) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, date);
    }

    @Override
    public String toString() {
        return "CalendarEvents{" +
                "id=" + id +
                ", eventId=" + eventId +
                ", date=" + date +
                '}';
    }
}
