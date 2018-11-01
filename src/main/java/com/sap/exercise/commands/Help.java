package com.sap.exercise.commands;

import com.sap.exercise.commands.util.CommandUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import java.io.PrintWriter;

import static com.sap.exercise.Main.OUTPUT;

public class Help implements Command {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String... args) {
        /*
        if no arguments are present -> display pseudo man page for application
        if an argument is present -> display helper for that argument
         */
        try {
            CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.helpOptions(), args);
            if (cmd.getOptions().length > 1) {
                throw new IllegalArgumentException("Too many arguments");
            }

            HelpFormatter formatter = new HelpFormatter();
            if (cmd.hasOption("ad")) {
                formatter.printHelp(new PrintWriter(OUTPUT), HelpFormatter.DEFAULT_WIDTH, "add", "header",
                        CommandUtils.addOptions(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, "footer", true);
            } else if (cmd.hasOption('e')) {
                //edit helper
                printer.println("edit command help");
            } else if (cmd.hasOption('d')) {
                formatter.printHelp(new PrintWriter(OUTPUT), HelpFormatter.DEFAULT_WIDTH, "delete", "header",
                        CommandUtils.timeFrameOptions(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, "footer", true);
            } else if (cmd.hasOption("ag")) {
                formatter.printHelp(new PrintWriter(OUTPUT), HelpFormatter.DEFAULT_WIDTH, "agenda", "header",
                        CommandUtils.timeFrameOptions(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, "footer", true);
            } else if (cmd.hasOption('c')) {
                formatter.printHelp(new PrintWriter(OUTPUT), HelpFormatter.DEFAULT_WIDTH, "calendar", "header",
                        CommandUtils.calendarOptions(), HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, "footer", true);
            } else {
                //print help in general
                printer.println("general help");
            }
        } catch (ParseException e) {
            printer.println(e.getMessage());
        }
    }

}
