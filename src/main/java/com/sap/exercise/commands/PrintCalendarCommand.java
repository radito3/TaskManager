package com.sap.exercise.commands;

import com.sap.exercise.handler.EventGetter;
import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;

import java.util.Calendar;

public class PrintCalendarCommand implements Command {

    private PrintCalendarOptions options;
    private boolean withEvents;

    public PrintCalendarCommand(PrintCalendarOptions options, boolean withEvents) {
        this.options = options;
        this.withEvents = withEvents;
    }

    @Override
    public CommandExecutionResult execute() {
        OutputPrinter printer = OutputPrinterProvider.getPrinter();
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
        return CommandExecutionResult.SUCCESSFUL;
    }

    public enum PrintCalendarOptions {
        ONE,
        THREE,
        YEAR;

        private String arg;

        public String getArgument() {
            return arg;
        }

        public void setArgument(String arg) {
            this.arg = arg;
        }
    }
}
