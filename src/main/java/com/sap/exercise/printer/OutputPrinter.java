package com.sap.exercise.printer;

import com.sap.exercise.handler.EventsGetterHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class OutputPrinter implements Closeable {

    private Calendar calendar = Calendar.getInstance();

    private PrintStream writer;

    OutputPrinter(OutputStream out) {
        writer = new PrintStream(out);
    }

    public void println(String val) {
        writer.println(val);
    }

    public void println() {
        writer.println();
    }

    public void print(String val) {
        writer.print(val);
    }

    public void printMonthCalendar(EventsGetterHandler handler, int month, boolean withEvents) {
        this.printCalendar(handler, month, calendar.get(Calendar.YEAR), false, withEvents);
    }

    public void printYearCalendar(EventsGetterHandler handler, int year, boolean withEvents) {
        writer.println(StringUtils.leftPad(String.valueOf(year), 14));
        writer.println();
        for (int i = 0; i < 12; i++) {
            this.printCalendar(handler, i, year, true, withEvents);
            writer.println();
        }
    }

    public void printEvents(Set<Event> events) {
        Map<Event, PrinterUtils.Formatter> eventFormatters = new HashMap<>(events.size());

        PrinterUtils.mapAndSort(writer, eventFormatters, events)
                .forEach((calendar, eventList) -> {
                    Date date = calendar.getTime();
                    writer.print(PrinterColors.YELLOW + date.toString().substring(0, 10) + PrinterColors.RESET);

                    eventList.forEach(event -> {
                        eventFormatters.get(event).printTime(date);
                        eventFormatters.get(event).printTitle(event.getTitle());
                        writer.println();
                    });

                    writer.println();
                });
    }

    private void printCalendar(EventsGetterHandler handler, int arg, int arg1, boolean wholeYear, boolean withEvents) {
        int month = arg > 11 ? (arg - 12) + 1 : arg < 0 ? (arg + 12) + 1 : arg + 1;
        int year = arg > 11 ? arg1 + 1 : arg < 0 ? arg1 - 1 : arg1;

        Calendar cal = new GregorianCalendar(year, month - 1, 1);

        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String weekDays = "Su  Mo  Tu  We  Th  Fr  Sa";
        String monthHeader = StringUtils.center(monthName + (wholeYear ? " " + year : ""), weekDays.length());

        int firstWeekdayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        int numberOfMonthDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        writer.println(monthHeader);
        writer.println(weekDays);

        int weekdayIndex = 0;

        for (int day = 1; day < firstWeekdayOfMonth; day++) {
            writer.print("    ");
            weekdayIndex++;
        }

        if (withEvents) {
            printWithEvents(handler, year, month, weekdayIndex, numberOfMonthDays);
        } else {
            for (int day = 1; day <= numberOfMonthDays; day++) {
                PrinterUtils.printDay(writer, day, month, year, "");

                weekdayIndex++;
                if (weekdayIndex == 7) {
                    weekdayIndex = 0;
                    writer.println();
                } else {
                    writer.print("  ");
                }
            }
        }
        writer.println();
    }

    private void printWithEvents(EventsGetterHandler handler, int year, int month, int weekdayIndex, int numOfMonthDays) {
        AtomicInteger weekdayInd = new AtomicInteger(weekdayIndex);

        Set<Event> events = handler.getEventsInTimeFrame(
                year + "-" + month + "-1",
                year + "-" + month + "-" + numOfMonthDays
        );

        PrinterUtils.monthEventsSorted(month, year, numOfMonthDays, events)
                .forEach((calendar, eventSet) -> {
                    PrinterUtils.printDay(writer,
                            calendar.get(Calendar.DAY_OF_MONTH),
                            month,
                            calendar.get(Calendar.YEAR),
                            eventSet.isEmpty() ? "" : (PrinterColors.CYAN_BACKGROUND + PrinterColors.BLACK));

                    weekdayInd.incrementAndGet();
                    if (weekdayInd.get() == 7) {
                        weekdayInd.set(0);
                        writer.println();
                    } else {
                        writer.print("  ");
                    }
                });
    }

    @Override
    public void close() {
        writer.close();
    }
}
