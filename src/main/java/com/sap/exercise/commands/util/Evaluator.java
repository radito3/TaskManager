package com.sap.exercise.commands.util;

import com.sap.exercise.handler.DateHandler;

import java.util.Calendar;
import java.util.function.BiFunction;

public interface Evaluator {

    <T> T evaluate(BiFunction<String, String, T> func);

    short getNumOfArgs();

    default String[] getWeekTimeFrame() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1;
        int day = today.get(Calendar.DAY_OF_MONTH);
        String todayStr = DateHandler.stringifyDate(year, month, day);

        today.add(Calendar.DAY_OF_MONTH, 6);

        return new String[] { todayStr, DateHandler.stringifyDate(year, month, today.get(Calendar.DAY_OF_MONTH)) };
    }

}
