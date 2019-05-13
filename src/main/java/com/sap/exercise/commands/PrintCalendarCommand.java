package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Calendar;

public class PrintCalendarCommand implements Command {

    private CommandLine cmd;

    public PrintCalendarCommand(CommandLine cmd) {
        this.cmd = cmd;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            Calendar calendar = Calendar.getInstance();
            EventsGetterHandler eventsGetter = new EventGetter();

            if (cmd.hasOption('3')) {
                for (int i = -1; i < 2; i++) {
                    printer.monthCalendar(eventsGetter, calendar.get(Calendar.MONTH) + i, cmd.hasOption('e'));
                    printer.println();
                }
            } else if (cmd.hasOption('y')) {
                int year = cmd.getOptionValues('y') == null ?
                        calendar.get(Calendar.YEAR) : Integer.valueOf(cmd.getOptionValue('y'));
                printer.yearCalendar(eventsGetter, year, cmd.hasOption('e'));
            } else {
                printer.monthCalendar(eventsGetter, calendar.get(Calendar.MONTH), cmd.hasOption('e'));
            }
        } catch (IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }

    public static Options getOptions() {
        Option one = Option.builder("1")
                .required(false)
                .longOpt("one")
                .desc("Display one month")
                .build();
        Option three = Option.builder("3")
                .required(false)
                .longOpt("three")
                .desc("Display three months")
                .build();
        Option year = Option.builder("y")
                .required(false)
                .longOpt("year")
                .hasArg()
                .optionalArg(true)
                .desc("Display the whole year (default argument is current year)")
                .build();
        Option withEvents = Option.builder("e")
                .required(false)
                .longOpt("events")
                .desc("Display calendar with events highlighted")
                .build();
        return CommandUtils.buildOptions(one, three, year, withEvents);
    }
}
