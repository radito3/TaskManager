package com.sap.exercise.commands.util;

import java.util.function.BiFunction;

public interface Evaluator {

    <T> T evaluate(BiFunction<String, String, T> func);

    default String[] getArgs() {
        int[] today = CommandUtils.getToday();
        int year = today[2], month = today[1], day = today[0];

        int[] inOneWeek = CommandUtils.getTime(day + 7, month, year);

        return new String[] { String.valueOf(year + "-" + month + "-" + day),
                String.valueOf(inOneWeek[2] + "-" + inOneWeek[1] + "-" + inOneWeek[0]) };
    }
}
