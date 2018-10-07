package com.sap.exercise.builder;

public abstract class InputArgs {

    private String value;

    private InputArgs(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static class Title extends InputArgs {

        private Title(String value) {
            super(value);
        }

        public static Title build(String val) {
            return new Title(val);
        }
    }

    public static class When extends InputArgs {

        private When(String value) {
            super(value);
        }

        public static When build(String val) {
            return new When(val);
        }
    }

    public static class Location extends InputArgs {

        private Location(String value) {
            super(value);
        }

        public static Location build(String val) {
            return new Location(val);
        }
    }

    public static class Description extends InputArgs {

        private Description(String value) {
            super(value);
        }

        public static Description build(String val) {
            return new Description(val);
        }
    }

    public static class AllDay extends InputArgs {

        private AllDay(String value) {
            super(value);
        }

        public static AllDay build(String val) {
            return new AllDay(val);
        }
    }

    public static class Duration extends InputArgs {

        private Duration(String value) {
            super(value);
        }

        public static Duration build(String val) {
            return new Duration(val);
        }
    }

    public static class Reminder extends InputArgs {

        private Reminder(String value) {
            super(value);
        }

        public static Reminder build(String val) {
            return new Reminder(val);
        }
    }

    public static class Repeat extends InputArgs {

        private Repeat(String value) {
            super(value);
        }

        public static Repeat build(String val) {
            return new Repeat(val);
        }
    }
}
