package com.sap.exercise.printer;

import com.sap.exercise.handler.Dao;
import com.sap.exercise.handler.TimeFrameCondition;
import com.sap.exercise.model.Event;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class OutputPrinter implements Closeable {

    private PrintStream printer;
    private final String weekDays = "Mo  Tu  We  Th  Fr  Sa  Su";

    OutputPrinter(OutputStream out) {
        printer = new PrintStream(out, true);
    }

    public void println(String val) {
        printer.println(val);
    }

    public void println() {
        printer.println();
    }

    public void print(String val) {
        printer.print(val);
    }

    public void printf(String format, Object... params) {
        printer.printf(format, params);
    }

    public void printStackTrace(Exception e) {
        e.printStackTrace(printer);
    }

    public void printHelp(String cmdLineSyntax, String header, String footer, Options options) {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setSyntaxPrefix("Usage: ");
        helpFormatter.setNewLine(System.lineSeparator());
        helpFormatter.printHelp(new PrintWriter(printer, true),
                74, cmdLineSyntax, header, options, 1, 3, footer, true);
    }

    public void printMonthCalendar(Dao<Event> handler, int month, boolean withEvents) {
        this.printCalendar(handler, month, LocalDate.now().getYear(), false, withEvents);
    }

    public void printYearCalendar(Dao<Event> handler, int year, boolean withEvents) {
        printer.println(StringUtils.center(String.valueOf(year), weekDays.length()));
        printer.println();
        for (int i = 0; i < 12; i++) {
            this.printCalendar(handler, i, year, true, withEvents);
            printer.println();
        }
    }

    public void printEvents(Collection<Event> events) {
        Map<LocalDate, Set<Event>> eventsPerDay = PrinterUtils.getEventsPerDay(events);
        Map<Event, Formatter> eventFormatters = PrinterUtils.getEventFormatters(eventsPerDay);

        for (Map.Entry<LocalDate, Set<Event>> entry : eventsPerDay.entrySet()) {
            String date = DateTimeFormatter.ofPattern("a MMM dd").format(entry.getKey());
            printer.print(PrinterColors.YELLOW + date + PrinterColors.RESET);

            for (Event event : entry.getValue()) {
                eventFormatters.get(event).printTime(printer);
                eventFormatters.get(event).printTitle(printer);
                printer.println();
            }

            printer.println();
        }
    }

    private void printCalendar(Dao<Event> handler, int arg, int arg1, boolean wholeYear, boolean withEvents) {
        int month = arg > 11 ? (arg - 12) + 1 : arg < 0 ? (arg + 12) + 1 : arg + 1;
        int year = arg > 11 ? arg1 + 1 : arg < 0 ? arg1 - 1 : arg1;

        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        YearMonth yearMonth = YearMonth.from(firstDayOfMonth);

        String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String monthHeader = StringUtils.center(monthName + (!wholeYear ? " " + year : ""), weekDays.length());

        int firstWeekdayOfMonth = firstDayOfMonth.getDayOfWeek().getValue();
        int numberOfMonthDays = yearMonth.lengthOfMonth();
        int weekdayIndex = 0;

        printer.println(monthHeader);
        printer.println(weekDays);

        for (int day = 1; day < firstWeekdayOfMonth; day++) {
            printer.print("    ");
            weekdayIndex++;
        }

        Map<LocalDate, Set<Event>> eventsPerDay = Collections.emptyMap();

        if (withEvents) {
            //TODO implement this query to work
            // SELECT CAST(CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS BIT)
            // FROM Eventt WHERE DATE(TimeOf) = <date>;
            // so as not to need to query all events in the time frame
            Collection<Event> events = handler.getAll(new TimeFrameCondition(
                year + "-" + month + "-1",
                year + "-" + month + "-" + numberOfMonthDays));
            eventsPerDay = PrinterUtils.getEventsPerDay(events);
        }

        for (int day = 1; day <= numberOfMonthDays; day++) {
            LocalDate date = LocalDate.of(year, month, day);
            boolean hasEvents = withEvents && !eventsPerDay.getOrDefault(date, Collections.emptySet())
                                                           .isEmpty();

            PrinterUtils.printDay(printer, date, hasEvents ? PrinterColors.CYAN_BACKGROUND + PrinterColors.BLACK : "");

            weekdayIndex++;
            if (weekdayIndex == 7) {
                weekdayIndex = 0;
                printer.println();
            } else {
                printer.print("  ");
            }
        }

        printer.println();
    }

    @Override
    public void close() {
        printer.close();
    }
}
