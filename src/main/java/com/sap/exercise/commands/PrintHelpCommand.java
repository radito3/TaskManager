package com.sap.exercise.commands;

import com.sap.exercise.util.DateHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class PrintHelpCommand extends AbstractCommand implements Command {

    @Override
    public int execute(String... args) {
        try {
            // Take a look at first comment inside execute method of AddCommand class
            CommandLine cmd = CommandUtils.getParsedCmd(getOptions(), args);

            if (cmd.getOptions().length > 1) {
                throw new IllegalArgumentException("Invalid number of arguments");
            }

            HelpFormatter helpFormatter = new HelpFormatter();
            String header, footer = "\nThe time options accept time formats as follows:\n" +
                    String.join("\n", DateHandler.DATE_FORMATS);

            if (cmd.hasOption("ad")) {
                header = "\nCreate an event\n\n";
                footer = "\nNote: Only one of the options can be present";
                helpFormatter.printHelp("add", header, AddCommand.getOptions(), footer, true);

            } else if (cmd.hasOption('e')) {
                header = "\nEdit an event";
                footer = "";
                helpFormatter.printHelp("edit <event name>", header, new Options(), footer, true);

            } else if (cmd.hasOption('d')) {
                header = "\nDeleteCommand an event\n\n";
                helpFormatter.printHelp("delete <event name>", header, DeleteCommand.getOptions(), footer, true);

            } else if (cmd.hasOption("ag")) {
                header = "\nDisplay a weekly agenda (if not given time arguments)\n\n";
                helpFormatter.printHelp("agenda", header, PrintAgendaCommand.getOptions(), footer, true);

            } else if (cmd.hasOption('c')) {
                header = "\nDisplay a calendar\n\n";
                footer = "\nNote: Only one of the options can be present";
                helpFormatter.printHelp("calendar", header, getOptions(), footer, true);

            } else {
                printer.println("Available options for help:\n" +
                        " -ad, --add      Show help page for Add command\n" +
                        " -e, --edit      Show help page for Edit command\n" +
                        " -d, --delete    Show help page for DeleteCommand command\n" +
                        " -ag, --agenda   Show help page for Agenda command\n" +
                        " -c, --calendar  Show help page for Calendar command");
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    private Options getOptions() {
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
