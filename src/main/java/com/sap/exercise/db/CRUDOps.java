package com.sap.exercise.db;

import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CRUDOps<T extends AbstractModel> extends GetFromDB, ProcessDB {

    Serializable create(T obj);

    void create(Collection<T> collection);

    void update(T obj);

    void delete(T obj);

    T getObjById(Integer id);

    Optional<T> getObjByProperty(String property, String value);

    List<CalendarEvents> getEventsInTimeFrame(String start, String end);

    void deleteEventsInTimeFrame(Event event, String start, String end);
}
