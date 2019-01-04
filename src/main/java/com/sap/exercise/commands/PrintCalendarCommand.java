package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.handler.EventsMapHandler;
import com.sap.exercise.handler.ThreadPoolHandler;
import com.sap.exercise.util.CommandUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.util.Calendar;

public class PrintCalendarCommand implements Command {

    private ThreadPoolHandler thPool;
    private EventsMapHandler mapHandler;

    public PrintCalendarCommand(ThreadPoolHandler thPool, EventsMapHandler mapHandler) {
        this.thPool = thPool;
        this.mapHandler = mapHandler;
    }

    @Override
    public int execute(String... args) {
        try {
            Calendar cal = Calendar.getInstance();
            CommandLine cmd = CommandUtils.getParsedCmd(CommandUtils.calendarOptions(), args);
            EventsGetterHandler handler = new EventGetter(thPool, mapHandler);

            if (cmd.getOptions().length > 2 || CommandUtils.optionsSizeWithoutEvents(cmd) > 1) {
                throw new IllegalArgumentException("Invalid number of arguments");
            }

            if (cmd.hasOption('3')) {
                for (int i = -1; i < 2; i++) {
                    printer.monthCalendar(handler, cal.get(Calendar.MONTH) + i, cmd.hasOption('e'));
                    printer.println();
                }
            } else if (cmd.hasOption('y')) {
                int year = cmd.getOptionValues('y') == null ? cal.get(Calendar.YEAR) : Integer.valueOf(cmd.getOptionValue('y'));
                printer.yearCalendar(handler, year, cmd.hasOption('e'));
            } else {
                printer.monthCalendar(handler, cal.get(Calendar.MONTH), cmd.hasOption('e'));
            }
        } catch (ParseException | IllegalArgumentException e) {
            printer.println(e.getMessage());
        }
        return 0;
    }
}
