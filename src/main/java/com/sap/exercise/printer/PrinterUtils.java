package com.sap.exercise.printer;

import com.sap.exercise.model.Event;

import java.util.List;

class PrinterUtils {

    static String getMonth(int month) {
        switch (month + 1) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            default:
                return "December";
        }
    }

    static String getDayOfWeek(int day) {
        switch (day) {
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "Sunday";
        }
    }

    static String format(List<Event> args) {
        final StringBuilder result = new StringBuilder();
        args.forEach(event -> {
            //get longest date argument
            //get longest month name
            //left pad words correctly according to those variables
        });
        return result.toString();
    }
}
