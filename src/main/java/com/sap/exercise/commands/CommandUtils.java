package com.sap.exercise.commands;

public class CommandUtils {

    private CommandUtils() {
    }

    //may move this to get rid of this class
    public enum PrintCalendarOptions {
        ONE,
        THREE,
        YEAR;

        private String arg;

        public String getArgument() {
            return arg;
        }

        public void setArgument(String arg) {
            this.arg = arg;
        }
    }
}
