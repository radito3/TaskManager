package com.sap.exercise.model;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BaseEvent {

    public abstract Integer getId();

    /**
     * Filter the input data when updating an object.
     *
     * (Since Hibernate doesn't allow this in the models, this needs to happen in the controller)
     * (The MVC controller is {@link com.sap.exercise.handler.EventsHandler})
     *
     * @param obj The input data
     * @param condition The filter data
     * @param supplier A supplier of an Exception class when the filter condition is not fulfilled
     * @param <T> The input data type
     * @param <X> The Exception type
     * @return The input data if it fulfills the filter condition
     */
    @Deprecated
    protected <T, X extends RuntimeException> T filter(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.of(obj).filter(condition).orElseThrow(supplier);
    }
}
