package com.sap.exercise.handler;

import com.sap.exercise.persistence.Property;

import java.util.Collection;
import java.util.Optional;

//data access object
public interface Dao<T> {

    <Y> Optional<T> get(Property<Y> property);

    Collection<T> getAll(CrudOptions options);

    void save(T arg);

    void update(T arg);

    void delete(T arg, CrudOptions options);
}
