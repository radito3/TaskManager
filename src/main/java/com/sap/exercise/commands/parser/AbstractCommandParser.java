package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.helper.CommandHelper;
import com.sap.exercise.commands.validator.CommandValidator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

abstract class AbstractCommandParser implements CommandParser {

    private CommandHelper helper;
    private CommandValidator validator;

    AbstractCommandParser(CommandHelper helper) {
        this.helper = helper;
    }

    AbstractCommandParser(CommandHelper helper, CommandValidator validator) {
        this(helper);
        this.validator = validator;
    }

    @Override
    public Command parse(String[] args) throws ParseException {
        Options options = getOptions();
        CommandLine cmd = new DefaultParser().parse(options, args, false);

        if (validator != null) {
            validator.validate(cmd);
        }

        if (cmd.hasOption("help")) {
            helper.printHelp(options);
            return () -> CommandExecutionResult.SUCCESSFUL;
        }

        return parseInternal(cmd);
    }
    
    abstract Command parseInternal(CommandLine cmd);

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
