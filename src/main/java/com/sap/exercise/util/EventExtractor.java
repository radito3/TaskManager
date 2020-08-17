package com.sap.exercise.util;

import com.sap.exercise.model.Event;

import java.time.YearMonth;
import java.util.Collection;

@FunctionalInterface
public interface EventExtractor {

    Collection<Event> getEventsForMonth(YearMonth yearMonth);
}
