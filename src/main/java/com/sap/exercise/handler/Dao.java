package com.sap.exercise.handler;

import com.sap.exercise.persistence.Property;

import java.util.Collection;
import java.util.Optional;

//data access object
//TODO change to Spring Data interface
// CrudRepository<Event, Integer>
public interface Dao<T> {

    <Y> Optional<T> get(Property<Y> property);

    Collection<T> getAll(CrudCondition condition);

    void save(T arg);

    void update(T arg);

    void delete(T arg, CrudCondition condition);
}
