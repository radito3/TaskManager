package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.CommandExecutionException;

import java.util.Calendar;

public class PrintCalendarCommand implements Command {

    private CommandUtils.PrintCalendarOptions options;
    private boolean withEvents;

    public PrintCalendarCommand(CommandUtils.PrintCalendarOptions options, boolean withEvents) {
        this.options = options;
        this.withEvents = withEvents;
    }

    @Override
    public int execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
        try {
            Calendar calendar = Calendar.getInstance();
            EventsGetterHandler eventsGetter = new EventGetter();

            switch (options) {
                case ONE:
                    printer.printMonthCalendar(eventsGetter, calendar.get(Calendar.MONTH), withEvents);
                    break;
                case THREE:
                    for (int i = -1; i < 2; i++) {
                        printer.printMonthCalendar(eventsGetter, calendar.get(Calendar.MONTH) + i, withEvents);
                        printer.println();
                    }
                    break;
                case YEAR:
                    int year = options.getArgument() == null ?
                            calendar.get(Calendar.YEAR) : Integer.valueOf(options.getArgument());
                    printer.printYearCalendar(eventsGetter, year, withEvents);
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new CommandExecutionException(e);
        }
        return 0;
    }
}
