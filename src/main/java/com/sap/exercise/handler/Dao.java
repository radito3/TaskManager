package com.sap.exercise.handler;

import java.util.Collection;
import java.util.Optional;

//data access object
public interface Dao<T> {

    Optional<T> get(Object property);

    Collection<T> getAll(CrudOptions options);

    void save(T arg);

    void update(T arg);

    void delete(T arg, CrudOptions options);
}
