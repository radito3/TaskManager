package com.sap.exercise.model;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class BaseEvent {

    public abstract Integer getId();

    protected <T, X extends RuntimeException> T filter(T obj, Predicate<T> condition, Supplier<X> supplier) {
        return Optional.of(obj).filter(condition).orElseThrow(supplier);
    }
}
