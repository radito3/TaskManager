package com.sap.exercise.commands.util;

import java.util.function.BiFunction;
import java.util.stream.Stream;

public interface Evaluator {

    <T> T evaluate(BiFunction<String, String, T> func);

    default String[] getWeekTimeFrame() {
        int[] today = CommandUtils.getToday();
        int year = today[2], month = today[1], day = today[0];

        int[] inOneWeek = CommandUtils.getTime(day + 6, month, year);

        return new String[] { stringifyDate(year, month, day), stringifyDate(inOneWeek[2], inOneWeek[1], inOneWeek[0]) };
    }

    static String stringifyDate(int year, int month, int day) {
        return Stream.of(year, month, day)
                .reduce("", (a, b) -> a + "-" + b, String::concat)
                .substring(1);
    }
}
