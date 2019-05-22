package com.sap.exercise.commands;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandUtils {

    private CommandUtils() {
    }

    public static Options buildOptions(Option... options) {
        Options result = new Options();
        for (Option opt : options) {
            result.addOption(opt);
        }
        return result;
    }

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
