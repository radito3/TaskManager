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
                                  .filter(str -> !str.isEmpty())
                                  .collect(Collectors.toList());
        numOfArgs = args.size();

        switch (numOfArgs) {
            case 0:
                DateParser today = new DateParser();
                arg1 = today.asString();
                arg2 = today.addOneWeek().asString();
                break;
            case 1:
                forOneArg(args.get(0));
                break;
            case 2:
                arg1 = new DateParser(var1).asString();
                arg2 = new DateParser(var2).asString();
                break;
        }
    }

    private void forOneArg(String arg) {
        DateParser date = new DateParser(arg);
        if (arg.endsWith("-")) {
            arg1 = new DateParser().asString();
            arg2 = date.asString();
        } else {
            arg1 = date.asString();
            arg2 = date.addOneWeek().asString();
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
