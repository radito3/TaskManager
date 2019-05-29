package com.sap.exercise.handler;

import javax.persistence.criteria.Predicate;
import java.util.Map;

public interface CrudOptions {

    Map<String, Object> getParameters();

    Predicate getPredicate();

    default boolean getCondition() {
        return true;
    }
}
