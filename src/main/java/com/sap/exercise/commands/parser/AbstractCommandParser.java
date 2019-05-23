package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandExecutionResult;
import com.sap.exercise.commands.helper.CommandHelper;
import com.sap.exercise.commands.validator.CommandValidator;
import com.sap.exercise.commands.validator.DefaultCommandValidator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.function.Function;

abstract class AbstractCommandParser implements CommandParser {

    private Function<Options, CommandHelper> helperCreator;
    CommandLine cmd;
    private Function<CommandLine, CommandValidator> validatorBuilder;

    AbstractCommandParser(Function<Options, CommandHelper> helperCreator) {
        this.helperCreator = helperCreator;
    }

    AbstractCommandParser(Function<Options, CommandHelper> helperCreator,
                          Function<CommandLine, CommandValidator> validatorCreator) {
        this(helperCreator);
        this.validatorBuilder = validatorCreator;
    }

    AbstractCommandParser(final CommandHelper helper) {
        helperCreator = opts -> helper;
    }

    @Override
    public Command parse(String[] args) {
        cmd = CommandParser.safeParseCmd(getOptions(), args);
        CommandValidator validator;
        if (validatorBuilder == null)
            validator = new DefaultCommandValidator(cmd);
        else
            validator = validatorBuilder.apply(cmd);

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
}
