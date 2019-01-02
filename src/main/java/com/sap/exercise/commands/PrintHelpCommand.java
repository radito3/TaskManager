package com.sap.exercise.commands;

import com.sap.exercise.util.CommandUtils;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PrintHelpCommand implements Command {

    @Override
    public int execute(String... args) {
        try {
            CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.helpOptions(), args);

            if (cmd.getOptions().length > 1) {
                throw new IllegalArgumentException("Invalid number of arguments");
            }

            HelpFormatter formatter = new HelpFormatter();
            String header, footer = "\nThe time options accept time formats as follows:\n" +
                    String.join("\n", DateHandler.DATE_FORMATS);

            if (cmd.hasOption("ad")) {
                header = "\nCreate an event\n\n";
                footer = "\nNote: Only one of the options can be present";
                formatter.printHelp("add", header, CommandUtils.addOptions(), footer, true);

            } else if (cmd.hasOption('e')) {
                header = "\nEdit an event";
                footer = "";
                formatter.printHelp("edit <event name>", header, new Options(), footer, true);

            } else if (cmd.hasOption('d')) {
                header = "\nDelete an event\n\n";
                formatter.printHelp("delete <event name>", header, CommandUtils.timeFrameOptions(true), footer, true);

            } else if (cmd.hasOption("ag")) {
                header = "\nDisplay a weekly agenda (if not given time arguments)\n\n";
                formatter.printHelp("agenda", header, CommandUtils.timeFrameOptions(false), footer, true);

            } else if (cmd.hasOption('c')) {
                header = "\nDisplay a calendar\n\n";
                footer = "\nNote: Only one of the options can be present";
                formatter.printHelp("calendar", header, CommandUtils.calendarOptions(), footer, true);

            } else {
                printer.println("Available options for help:\n" +
                        " -ad, --add      Show help page for Add command\n" +
                        " -e, --edit      Show help page for Edit command\n" +
                        " -d, --delete    Show help page for Delete command\n" +
                        " -ag, --agenda   Show help page for Agenda command\n" +
                        " -c, --calendar  Show help page for Calendar command");
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

}
