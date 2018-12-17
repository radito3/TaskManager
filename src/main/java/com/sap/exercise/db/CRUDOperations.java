package com.sap.exercise.db;

import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.CalendarEvents;
import com.sap.exercise.model.Event;
import com.sap.exercise.model.User;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CRUDOperations<T extends AbstractModel> implements CRUDOps<T> {

    private Class<T> tClass;
    private Map<Class<? extends AbstractModel>, String> modelNames = new HashMap<>(3);

    public CRUDOperations(Class<T> tClass) {
        this.tClass = tClass;
        modelNames.put(Event.class, "Eventt");
        modelNames.put(CalendarEvents.class, "CalendarEvents");
        modelNames.put(User.class, "User");
    }

    @Override
    public Serializable create(T obj) {
        return get(s -> s.save(obj));
    }

    @Override
    public void create(Collection<T> collection) {
        process(s -> collection.forEach(s::save));
    }

    @Override
    public void update(T obj) {
        process(s -> s.update(obj));
    }

    @Override
    public void delete(T obj) {
        process(s -> s.delete(obj));
    }

    @Override
    public T getObjById(Integer id) {
        return get(s -> s.get(tClass, id));
    }

    @Override
    public Optional<T> getObjByProperty(String property, String value) {
        return get(s -> s.createNativeQuery("SELECT * FROM " + modelNames.get(tClass) + " WHERE " + property + " = \'"
                + value + "\' LIMIT 1;", tClass).uniqueResultOptional());
    }

    @Override
    public List<CalendarEvents> getEventsInTimeFrame(String start, String end) {
        return get(s -> s.createNativeQuery("SELECT * FROM CalendarEvents WHERE Date >= \'" + start +
                "\' AND Date <= \'" + end + "\';", CalendarEvents.class).getResultList());
    }

    @Override
    public void deleteEventsInTimeFrame(Event event, String start, String end) {
        process(s -> s.createNativeQuery("DELETE FROM CalendarEvents WHERE EventId = " + event.getId() +
                " AND Date >= \'" + start + "\' AND Date <= \'" + end + "\';").executeUpdate());
    }
}
