package com.sap.exercise.commands.util;

import java.util.function.BiFunction;

public class OneArgEvaluator implements Evaluator {

    private String arg1, arg2;

    OneArgEvaluator(String arg) {
        if (arg.endsWith("-")) {
            arg1 = getWeekTimeFrame()[0];
            arg2 = arg.substring(0, arg.length() - 1);
        } else {
            arg1 = arg;

            String[] date = arg.split("-");
            int[] inOneWeek = CommandUtils.getTime(Integer.valueOf(date[2]) + 6, Integer.valueOf(date[1]), Integer.valueOf(date[0]));

            arg2 = Evaluator.stringifyDate(inOneWeek[2], inOneWeek[1], inOneWeek[0]);
        }
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }
}
