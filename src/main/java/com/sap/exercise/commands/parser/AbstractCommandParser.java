package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.helper.CommandHelper;
import com.sap.exercise.commands.validator.CommandValidator;
import com.sap.exercise.commands.validator.DefaultCommandValidator;
import com.sap.exercise.util.ExceptionMessageHandler;
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

    AbstractCommandParser(final CommandHelper helper) {
        helperCreator = opts -> helper;
    }

    @Override
    public Command parse(String[] args) {
        cmd = parseCmd(getOptions(), args);
        CommandValidator validator;
        if (validatorCreator == null)
            validator = new DefaultCommandValidator(cmd);
        else
            validator = validatorCreator.apply(cmd);

        if (!validator.isValid())
            return () -> CommandExecutionResult.ERROR;

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

    private static CommandLine parseCmd(Options options, String[] args) {
        try {
            return new DefaultParser().parse(options, args, false);
        } catch (ParseException e) {
            ExceptionMessageHandler.setMessage(e.getMessage());
            return null;
        }
    }

    static String buildEventName(String[] input) {
        if (input == null) {
            throw new IllegalArgumentException("Event name not specified");
        }
        return String.join(" ", input);
    }
}
