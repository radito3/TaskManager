package com.sap.exercise.commands.util;

import com.sap.exercise.handler.DateHandler;

import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Evaluator {

    <T> T evaluate(BiFunction<String, String, T> func);

    short getNumOfArgs();

    default String[] getWeekTimeFrame() {
        int[] today = DateHandler.getToday();
        int year = today[2], month = today[1], day = today[0];

        int[] inOneWeek = DateHandler.getTime(day + 6, month, year);

        return new String[] { stringifyDate(year, month, day), stringifyDate(inOneWeek[2], inOneWeek[1], inOneWeek[0]) };
    }

    static String stringifyDate(int year, int month, int day) {
        return Stream.of(year, month, day)
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
    }
}
