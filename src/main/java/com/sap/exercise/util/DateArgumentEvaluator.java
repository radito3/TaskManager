package com.sap.exercise.util;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateArgumentEvaluator {

    private String arg1, arg2;
    private int numOfArgs;

    public DateArgumentEvaluator(String var1, String var2) {
        List<String> args = Stream.of(var1, var2)
                                  .filter(String::isEmpty)
                                  .collect(Collectors.toList());
        numOfArgs = args.size();

        switch (numOfArgs) {
            case 0:
                forZeroArgs();
                break;
            case 1:
                forOneArg(args.get(0));
                break;
            case 2:
                arg1 = new DateHandler(var1).toString();
                arg2 = new DateHandler(var2).toString();
                break;
        }
    }

    private void forZeroArgs() {
        DateHandler date = new DateHandler();
        arg1 = date.toString();

        date.addOneWeek();
        arg2 = date.toString();
    }

    private void forOneArg(String arg) {
        DateHandler date = new DateHandler(arg);
        if (arg.endsWith("-")) {
            arg1 = new DateHandler().toString();
            arg2 = date.toString();
        } else {
            arg1 = date.toString();

            date.addOneWeek();
            arg2 = date.toString();
        }
    }

    public <T> T eval(BiFunction<String, String, T> func) {
        return func.apply(arg1, arg2);
    }

    public void eval(BiConsumer<String, String> cons) {
        cons.accept(arg1, arg2);
    }

    public int numOfArgs() {
        return numOfArgs;
    }
}
