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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OutputPrinter implements Closeable {

    private Calendar calendar = Calendar.getInstance();
    private PrintStream printer;
    private final String weekDays = "Su  Mo  Tu  We  Th  Fr  Sa";

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
        this.printCalendar(handler, month, calendar.get(Calendar.YEAR), false, withEvents);
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
        Map<Event, PrinterUtils.Formatter> eventFormatters = new HashMap<>(events.size());

        PrinterUtils.mapAndSort(printer, eventFormatters, events)
                .forEach((calendar, eventList) -> {
                    Date date = calendar.getTime();
                    printer.printf(PrinterColors.YELLOW + "%ta %1$tb %1$2te" + PrinterColors.RESET, date);

                    eventList.forEach(event -> {
                        eventFormatters.get(event).printTime(date);
                        eventFormatters.get(event).printTitle(event.getTitle());
                        printer.println();
                    });

                    printer.println();
                });
    }

    private void printCalendar(Dao<Event> handler, int arg, int arg1, boolean wholeYear, boolean withEvents) {
        int month = arg > 11 ? (arg - 12) + 1 : arg < 0 ? (arg + 12) + 1 : arg + 1;
        int year = arg > 11 ? arg1 + 1 : arg < 0 ? arg1 - 1 : arg1;

        Calendar cal = new GregorianCalendar(year, month - 1, 1);

        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String monthHeader = StringUtils.center(monthName + (!wholeYear ? " " + year : ""), weekDays.length());

        int firstWeekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int weekdayIndex = 0;

        printer.println(monthHeader);
        printer.println(weekDays);

        for (int day = 1; day < firstWeekdayOfMonth; day++) {
            printer.print("    ");
            weekdayIndex++;
        }

        if (withEvents) {
            printWithEvents(handler, year, month, weekdayIndex, numberOfMonthDays);
        } else {
            for (int day = 1; day <= numberOfMonthDays; day++) {
                PrinterUtils.printDay(printer, day, month, year, "");

                printWeekDaySpaces(weekdayIndex);
            }
        }
        printer.println();
    }

    private void printWeekDaySpaces(int weekdayIndex) {
        AtomicInteger weekdayInd = new AtomicInteger(weekdayIndex);
        weekdayInd.incrementAndGet();
        if (weekdayInd.get() == 7) {
            weekdayInd.set(0);
            printer.println();
        } else {
            printer.print("  ");
        }
    }

    private void printWithEvents(Dao<Event> handler, int year, int month, int weekdayIndex, int numOfMonthDays) {
        Collection<Event> events = handler.getAll(new TimeFrameCondition(
                year + "-" + month + "-1",
                year + "-" + month + "-" + numOfMonthDays
        ));

        PrinterUtils.monthEventsSorted(month, year, numOfMonthDays, events)
                .forEach((calendar, eventSet) -> {
                    PrinterUtils.printDay(printer,
                            calendar.get(Calendar.DAY_OF_MONTH),
                            month,
                            calendar.get(Calendar.YEAR),
                            eventSet.isEmpty() ? "" : (PrinterColors.CYAN_BACKGROUND + PrinterColors.BLACK));

                    printWeekDaySpaces(weekdayIndex);
                });
    }

    @Override
    public void close() {
        printer.close();
    }
}
