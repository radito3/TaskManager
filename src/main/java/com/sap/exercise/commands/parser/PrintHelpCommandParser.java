package com.sap.exercise.commands.parser;

import com.sap.exercise.commands.Command;
import com.sap.exercise.commands.CommandUtils;
import com.sap.exercise.commands.PrintHelpCommand;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class PrintHelpCommandParser implements CommandParser {

    @Override
    public Command parse(String[] args) {
        CommandLine cmd;
        if ((cmd = CommandParser.safeParseCmd(printHelpOptions(), args)) == null)
            return () -> 0;
        if (cmd.getOptions().length > 1) {
            throw new IllegalArgumentException("Invalid number of arguments");
        }
        return new PrintHelpCommand(cmd);
    }

    private Options printHelpOptions() {
        Option add = Option.builder("ad")
                .required(false)
                .longOpt("add")
                .build();
        Option edit = Option.builder("e")
                .required(false)
                .longOpt("edit")
                .build();
        Option delete = Option.builder("d")
                .required(false)
                .longOpt("delete")
                .build();
        Option agenda = Option.builder("ag")
                .required(false)
                .longOpt("agenda")
                .build();
        Option calendar = Option.builder("c")
                .required(false)
                .longOpt("calendar")
                .build();
        return CommandUtils.buildOptions(add, edit, delete, agenda, calendar);
    }
}
