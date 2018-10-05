package com.sap.exercise.builder;

public class InputFilterFactory {

    public static InputValueFilter getInputFilter(InputValueTypes type) {
        switch (type) {
            case CALENDAR:
                return new CalendarFilter();
            case INTEGER:
                return new IntegerFilter();
            case BOOL:
                return new BooleanFilter();
            case REPEAT:
                return new RepeatableFilter();
            default:
                return new StringFilter();
        }
    }
}
