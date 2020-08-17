package com.sap.exercise.printer;

import com.sap.exercise.model.Event;
import com.sap.exercise.util.EventExtractor;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Year;
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

    public void printMonthCalendar(int month, EventExtractor eventExtractor) {
        int year = Year.now().getValue();
        if (month < 1) {
            year--;
            month += 12;
        } else if (month > 12) {
            year++;
            month -= 12;
        }
        this.printCalendar(YearMonth.of(year, month), false, eventExtractor);
    }

    public void printYearCalendar(int year, EventExtractor eventExtractor) {
        printer.println(StringUtils.center(String.valueOf(year), weekDays.length()));
        printer.println();
        for (int i = 1; i <= 12; i++) {
            this.printCalendar(YearMonth.of(year, i), true, eventExtractor);
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

    private void printCalendar(YearMonth yearMonth, boolean wholeYear, EventExtractor eventExtractor) {
        String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        String monthHeader = StringUtils.center(monthName + (!wholeYear ? " " + yearMonth.getYear() : ""), weekDays.length());

        printer.println(monthHeader);
        printer.println(weekDays);

        LocalDate firstDayOfMonth = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        int firstWeekdayOfMonth = firstDayOfMonth.getDayOfWeek().getValue();
        int numberOfMonthDays = yearMonth.lengthOfMonth();
        int weekdayIndex = 0;

        for (int day = 1; day < firstWeekdayOfMonth; day++) {
            printer.print("    ");
            weekdayIndex++;
        }

        Collection<Event> events = eventExtractor.getEventsForMonth(yearMonth);
        Map<LocalDate, Set<Event>> eventsPerDay = PrinterUtils.getEventsPerDay(events);

        for (int day = 1; day <= numberOfMonthDays; day++) {
            LocalDate date = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), day);
            boolean hasEvents = !eventsPerDay.getOrDefault(date, Collections.emptySet())
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
