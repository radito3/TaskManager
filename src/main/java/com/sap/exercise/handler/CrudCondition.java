package com.sap.exercise.handler;

import javax.persistence.criteria.Predicate;
import java.util.Map;

public interface CrudCondition {

    Map<String, Object> parameters();

    Predicate queryCondition();

    default boolean isToBeExecuted() {
        return true;
    }
}
