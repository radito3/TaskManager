package com.sap.exercise.commands;

import com.sap.exercise.commands.parser.AddCommandParser;
import com.sap.exercise.commands.parser.DeleteCommandParser;
import com.sap.exercise.commands.parser.PrintAgendaCommandParser;
import com.sap.exercise.commands.parser.PrintCalendarCommandParser;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.CommandExecutionException;
import com.sap.exercise.util.DateHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class PrintHelpCommand implements Command {

    private CommandLine cmd;

    public PrintHelpCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            HelpFormatter helpFormatter = new HelpFormatter();
            String header, footer = "\nThe time options accept time formats as follows:\n" +
                    String.join("\n", DateHandler.DATE_FORMATS);

            if (cmd.hasOption("ad")) {
                header = "\nCreate an event\n\n";
                footer = "\nNote: Only one of the options can be present";
                helpFormatter.printHelp("add", header,
                        AddCommandParser.addCommandOptions(), footer, true);

            } else if (cmd.hasOption('e')) {
                header = "\nEdit an event";
                footer = "";
                helpFormatter.printHelp("edit <event name>", header, new Options(), footer, true);

            } else if (cmd.hasOption('d')) {
                header = "\nDelete an event\n\n";
                helpFormatter.printHelp("delete <event name>", header,
                        DeleteCommandParser.deleteOptions(), footer, true);

            } else if (cmd.hasOption("ag")) {
                header = "\nDisplay a weekly agenda (if not given time arguments)\n\n";
                helpFormatter.printHelp("agenda", header,
                        PrintAgendaCommandParser.printAgendaOptions(), footer, true);

            } else if (cmd.hasOption('c')) {
                header = "\nDisplay a calendar\n\n";
                footer = "\nNote: Only one of the options can be present (excluding --events)";
                helpFormatter.printHelp("calendar", header,
                        PrintCalendarCommandParser.printCalendarOptions(), footer, true);

            } else {
                printer.println("Available options for help:\n" +
                        " -ad, --add      Show help page for Add command\n" +
                        " -e, --edit      Show help page for Edit command\n" +
                        " -d, --delete    Show help page for DeleteEventCommand command\n" +
                        " -ag, --agenda   Show help page for Agenda command\n" +
                        " -c, --calendar  Show help page for Calendar command");
            }
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException(e);
        }
        return 0;
    }
}
