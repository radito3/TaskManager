package com.sap.exercise.filters;

public interface InputValueFilter<T> {

    T valueOf(String input);
}
