package com.sap.exercise.commands.util;

import java.util.Calendar;
import java.util.function.BiFunction;

public interface Evaluator {

    <T> T evaluate(BiFunction<String, String, T> func);

    default String[] getArgs() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR),
                month = cal.get(Calendar.MONTH),
                day = cal.get(Calendar.DAY_OF_MONTH);
        return new String[] { String.valueOf(year + "-" + month + "-" + day),
                String.valueOf(year + "-" + month + "-" + (day + 7)) };
    }
}
