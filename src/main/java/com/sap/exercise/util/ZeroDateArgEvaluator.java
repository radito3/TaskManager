package com.sap.exercise.util;

import java.util.function.BiFunction;

public class ZeroDateArgEvaluator implements Evaluator {

    private String arg1, arg2;

    ZeroDateArgEvaluator() {
        arg1 = new DateHandler(DateHandler.Dates.TODAY).asString();
        arg2 = new DateHandler(DateHandler.Dates.IN_ONE_WEEK).asString();
    }

    @Override
    public <T> T evaluate(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }

    @Override
    public short getNumOfArgs() {
        return 0;
    }
}
