package com.sap.exercise.commands;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.EventDao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.model.Event;
import com.sap.exercise.printer.OutputPrinter;
import com.sap.exercise.printer.OutputPrinterProvider;
import com.sap.exercise.util.EventExtractor;

import java.time.LocalDate;
import java.util.Collections;

public class PrintCalendarCommand implements Command {

    private final PrintCalendarOptions options;
    private final boolean withEvents;
    private final OutputPrinter printer = OutputPrinterProvider.getPrinter();

    public PrintCalendarCommand(PrintCalendarOptions options, boolean withEvents) {
        this.options = options;
        this.withEvents = withEvents;
    }

    @Override
    public CommandExecutionResult execute() {
        LocalDate today = LocalDate.now();
        switch (options) {
            case ONE:
                printer.printMonthCalendar(today.getMonthValue(), getEventExtractor());
                break;
            case THREE:
                for (int i = -1; i < 2; i++) {
                    printer.printMonthCalendar(today.getMonthValue() + i, getEventExtractor());
                    printer.println();
                }
                break;
            case YEAR:
                int year = options.getArgument() == null ? today.getYear() : Integer.parseInt(options.getArgument());
                printer.printYearCalendar(year, getEventExtractor());
                break;
        }
        return CommandExecutionResult.SUCCESSFUL;
    }

    private EventExtractor getEventExtractor() {
        return yearMonth -> {
            if (!withEvents) {
                return Collections.emptySet();
            }

            int year = yearMonth.getYear();
            int month = yearMonth.getMonthValue();
            Dao<Event> eventDao = new EventDao();

            //TODO implement this query to work
            // SELECT CAST(CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS BIT)
            // FROM Eventt WHERE DATE(TimeOf) = <date in array of month dates>
            // ORDER BY <date>;
            // so as not to need to query all events in the time frame
            // because the info that is needed here is just whether events exits on the days, not the events themselves
            return eventDao.getAll(new TimeFrameCondition(
                year + "-" + month + "-1",
                year + "-" + month + "-" + yearMonth.lengthOfMonth()));
        };
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
