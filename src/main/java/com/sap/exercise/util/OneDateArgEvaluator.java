package com.sap.exercise.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.BiFunction;

public class OneDateArgEvaluator implements Evaluator {

    private String arg1, arg2;

    OneDateArgEvaluator(String arg) {
        if (arg.endsWith("-")) {
            arg1 = new DateHandler(DateHandler.Dates.TODAY).asString();
            arg2 = arg.substring(0, arg.length() - 1);
        } else {
            arg1 = arg;

            String[] date = arg.split("[-./ ]");

            Calendar cal = new GregorianCalendar(Integer.valueOf(date[0]), Integer.valueOf(date[1]) - 1, Integer.valueOf(date[2]));
            cal.add(Calendar.DAY_OF_MONTH, 6);

            arg2 = new DateHandler(cal).asString();
        }
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }

    @Override
    public short getNumOfArgs() {
        return 1;
    }
}