package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.util.List;
import java.util.stream.Collectors;

class PrinterUtils {

    static String getMonth(int month, boolean flag) {
        switch (month) {
            case 1:
                return flag ? "Jan" : "January";
            case 2:
                return flag ? "Feb" : "February";
            case 3:
                return flag ? "Mar" : "March";
            case 4:
                return flag ? "Apr" : "April";
            case 5:
                return "May";
            case 6:
                return flag ? "Jun" : "June";
            case 7:
                return flag ? "Jul" : "July";
            case 8:
                return flag ? "Aug" : "August";
            case 9:
                return flag ? "Sep" : "September";
            case 10:
                return flag ? "Oct" : "October";
            case 11:
                return flag ? "Nov" : "November";
            default:
                return flag ? "Dec" : "December";
        }
    }

    static String getMonth(int month) {
        return getMonth(month, false);
    }

    static String getDayOfWeek(int day, boolean flag) {
        switch (day) {
            case 1:
                return flag ? "Mon" : "Monday";
            case 2:
                return flag ? "Tue" : "Tuesday";
            case 3:
                return flag ? "Wed" : "Wednesday";
            case 4:
                return flag ? "Thu" : "Thursday";
            case 5:
                return flag ? "Fri" : "Friday";
            case 6:
                return flag ? "Sat" : "Saturday";
            default:
                return flag ? "Sun" : "Sunday";
        }
    }

    static String getDayOfWeek(int day) {
        return getDayOfWeek(day, false);
    }

    static String format(List<Event> args) {
        int longestDate = 0;
        int longestName = 0;
        return args.stream()
                .map(Event::getTitle) //temporary
                .peek(event -> {
                    //get longest date argument
                    //get longest month name
                    //left pad words correctly according to those variables
                })
                .collect(Collectors.joining("\n"));
    }
}
