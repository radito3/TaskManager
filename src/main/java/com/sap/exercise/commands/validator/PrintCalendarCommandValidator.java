package com.sap.exercise.commands.validator;

import com.sap.exercise.util.ExceptionMessageHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PrintCalendarCommandValidator extends DefaultCommandValidator {

    public PrintCalendarCommandValidator(CommandLine cmd) {
        super(cmd);
    }

    @Override
    public boolean isValid() {
        boolean commonCondition = super.isValid();
        if (commonCondition && validateOptions()) {
            ExceptionMessageHandler.setMessage("Invalid number of arguments");
            return false;
        }
        return commonCondition;
    }

    private boolean validateOptions() {
        Supplier<Stream<Option>> optionsStreamSupplier = () -> Arrays.stream(cmd.getOptions());
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        Option help = Option.builder()
                .longOpt("help")
                .required(false)
                .desc("Print command help")
                .build();

        boolean onlyHelp = optionsStreamSupplier.get()
                .allMatch(option -> option.equals(help));
        boolean optWithoutEvents = optionsStreamSupplier.get()
                .filter(opt -> !opt.equals(withEvents))
                .count() > 1;
        boolean allOpts = cmd.getOptions().length > 2;

        return !onlyHelp & (optWithoutEvents | allOpts);
    }
}
