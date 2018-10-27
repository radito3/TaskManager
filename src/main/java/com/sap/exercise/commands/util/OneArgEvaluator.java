package com.sap.exercise.commands.util;

import com.sap.exercise.handler.DateHandler;

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
            int[] inOneWeek = DateHandler.inOneWeek(date[2], date[1], date[0]);

            arg2 = Evaluator.stringifyDate(inOneWeek[2], inOneWeek[1], inOneWeek[0]);
        }
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }
}
