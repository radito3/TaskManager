package com.sap.exercise.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Calendar")
public class Calendar extends AbstractModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "mysql->int(11)", name = "Id", nullable = false, unique = true)
    private Integer id;

    @Column(columnDefinition = "mysql->tinyint(4)", name = "Day", nullable = false)
    private Integer day;

    @Column(columnDefinition = "mysql->tinyint(4)", name = "Month", nullable = false)
    private Integer month;

    @Column(columnDefinition = "mysql->smallint(6)", name = "Year", nullable = false)
    private Integer year;

    public Calendar() {
        this(1, 1, 1970);
    }

    public Calendar(Integer day, Integer month, Integer year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar calendar = (Calendar) o;
        return Objects.equals(id, calendar.id) &&
                Objects.equals(day, calendar.day) &&
                Objects.equals(month, calendar.month) &&
                Objects.equals(year, calendar.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, day, month, year);
    }

    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                "}";
    }
}
