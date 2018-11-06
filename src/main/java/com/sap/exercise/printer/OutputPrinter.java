package com.sap.exercise.printer;

import com.sap.exercise.handler.DateHandler;
import com.sap.exercise.handler.EventHandler;
import com.sap.exercise.model.Event;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OutputPrinter {

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String INVERT = "\u001B[7m";

    public static final String CURSOR_UP = "\u001B[1000A";
    public static final String CURSOR_DOWN = "\u001B[1000B";
    public static final String CURSOR_RIGHT = "\u001B[1000C";
    public static final String CURSOR_LEFT = "\u001B[1000D";

    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[41m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String PURPLE_BACKGROUND = "\u001B[45m";
    public static final String CYAN_BACKGROUND = "\u001B[46m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";

    public static final String BLACK_BRIGHT = "\u001B[90m";
    public static final String RED_BRIGHT = "\u001B[91m";
    public static final String GREEN_BRIGHT = "\u001B[92m";
    public static final String YELLOW_BRIGHT = "\u001B[93m";
    public static final String BLUE_BRIGHT = "\u001B[94m";
    public static final String PURPLE_BRIGHT = "\u001B[95m";
    public static final String CYAN_BRIGHT = "\u001B[96m";
    public static final String WHITE_BRIGHT = "\u001B[97m";

    public static final String BLACK_BACKGROUND_BRIGHT = "\u001B[100m";
    public static final String RED_BACKGROUND_BRIGHT = "\u001B[101m";
    public static final String GREEN_BACKGROUND_BRIGHT = "\u001B[102m";
    public static final String YELLOW_BACKGROUND_BRIGHT = "\u001B[103m";
    public static final String BLUE_BACKGROUND_BRIGHT = "\u001B[104m";
    public static final String PURPLE_BACKGROUND_BRIGHT = "\u001B[105m";
    public static final String CYAN_BACKGROUND_BRIGHT = "\u001B[106m";
    public static final String WHITE_BACKGROUND_BRIGHT = "\u001B[107m";

    private Calendar calendar = Calendar.getInstance();

    private PrintStream writer;

    public OutputPrinter(OutputStream out) {
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

    public void error(String val) {
        writer.println(RED + val + RESET);
    }

    public void monthCalendar(int month, boolean withEvents) {
        this.printCalendar(month, calendar.get(Calendar.YEAR), false, withEvents);
    }

    public void yearCalendar(int year, boolean withEvents) {
        writer.println(StringUtils.leftPad(String.valueOf(year), 12));
        writer.println();
        for (int i = 0; i < 12; i++) {
            this.printCalendar(i, year, true, withEvents);
            writer.println();
        }
    }

    public void printEvents(Set<Event> events) {
        Event[] array = events.toArray(new Event[0]);
        Arrays.sort(array, Comparator.comparing(Event::getTimeOf));
        PrinterUtils.format(writer, Stream.of(array));
    }

    private void printCalendar(int arg, int arg1, boolean wholeYear, boolean withEvents) {
        int month = arg > 11 ? (arg - 12) + 1 : arg + 1;
        int year = arg > 11 ? arg1 + 1 : arg1;

        int[] days = DateHandler.getMonthDays();

        String text = StringUtils.leftPad(PrinterUtils.getMonth(month),
                PrinterUtils.getMonth(month).length() + (wholeYear ? 6 : 4));

        writer.println(text + " " + (wholeYear ? "" : year));
        writer.println(" S  M Tu  W Th  F  S");

        int startingDay = calendar.getFirstDayOfWeek() - 1;

        for (int i = 0; i < startingDay; i++)
            writer.print("   ");

        if (withEvents) {
            printWithEvents(month, startingDay);
        } else {
            for (int i = 1; i <= days[month]; i++) {
                printDay(i, month, year, "");

                if (((i + startingDay) % 7 == 0) || (i == days[month])) writer.println();
            }
        }
    }

    private void printDay(int day, int month, int year, String format) {
        if (isToday(day, month, year))
            writer.printf(INVERT + "%2d " + RESET, day);
        else
            writer.printf(format + "%2d " + RESET, day);
    }

    private boolean isToday(int day, int month, int year) {
        int[] today = DateHandler.getToday();
        return day == today[0] && month == today[1] && year == today[2];
    }

    private void printWithEvents(int month, int startingDay) {
        int[] today = DateHandler.getToday();
        Set<Event> events = EventHandler.getEventsInTimeFrame(
                today[2] + "-" + month + "-01",
                today[2] + "-" + month + "-" + DateHandler.getMonthDays()[month]
        );
        //only works with past events
        IntStream.rangeClosed(1, DateHandler.getMonthDays()[month])
                .mapToObj(i -> DateHandler.stringifyDate(today[2], month, i))
                .map(str -> new DateHandler(str).asCalendar())
                .collect(Collectors.toMap(keyMapper(), valueMapper(events)))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getKey().get(Calendar.DAY_OF_MONTH)))
                .forEach(entry -> {
                    Calendar date = entry.getKey();
                    Set<Event> eventSet = entry.getValue();

                    if (eventSet.isEmpty()) {
                        printDay(date.get(Calendar.DAY_OF_MONTH), month, date.get(Calendar.YEAR), "");
                    } else {
                        printDay(date.get(Calendar.DAY_OF_MONTH), month, date.get(Calendar.YEAR), CYAN_BACKGROUND + BLACK);
                    }

                    if (((date.get(Calendar.DAY_OF_MONTH) + startingDay) % 7 == 0) ||
                            (date.get(Calendar.DAY_OF_MONTH) == DateHandler.getMonthDays()[month])) {
                        writer.println();
                    }
                });
    }

    private Function<Calendar, Calendar> keyMapper() {
        return Function.identity();
    }

    private Function<Calendar, Set<Event>> valueMapper(Set<Event> events) {
        return (date) ->
                events.stream()
                        .filter(event -> DateUtils.truncatedCompareTo(date, event.getTimeOf(), Calendar.DAY_OF_MONTH) == 0)
                        .collect(Collectors.toSet());
    }

}
