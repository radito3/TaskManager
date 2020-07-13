package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.helper.CommandHelper;
import com.sap.exercise.commands.validator.CommandValidator;
import com.sap.exercise.commands.validator.DefaultCommandValidator;
import org.apache.commons.cli.*;

import java.util.function.Function;

abstract class AbstractCommandParser implements CommandParser {

    private Function<Options, CommandHelper> helperCreator;
    CommandLine cmd;
    private Function<CommandLine, CommandValidator> validatorCreator;

    AbstractCommandParser(Function<Options, CommandHelper> helperCreator) {
        this.helperCreator = helperCreator;
    }

    AbstractCommandParser(Function<Options, CommandHelper> helperCreator,
                          Function<CommandLine, CommandValidator> validatorCreator) {
        this(helperCreator);
        this.validatorCreator = validatorCreator;
    }

    AbstractCommandParser() {
    }

    @Override
    public Command parse(String[] args) throws ParseException {
        cmd = new DefaultParser().parse(getOptions(), args, false);
        CommandValidator validator = validatorCreator == null ? new DefaultCommandValidator(cmd) : validatorCreator.apply(cmd);

        validator.validate();

        if (cmd.hasOption("help")) {
            helperCreator.apply(getOptions()).printHelp();
            return () -> CommandExecutionResult.SUCCESSFUL;
        }

        return null;
    }

    abstract Options getOptions();

    static Options buildOptions(Option... options) {
        Options result = new Options();
        for (Option opt : options) {
            result.addOption(opt);
        }
        return result;
    }

    static String buildEventName(String[] input) {
        if (input == null || input.length == 0) {
            throw new IllegalArgumentException("Event name not specified");
        }
        return String.join(" ", input);
    }
}
