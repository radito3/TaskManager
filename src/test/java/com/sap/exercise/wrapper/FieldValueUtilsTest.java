package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FieldValueUtilsTest {

    @Test
    @DisplayName("Filter test with valid string argument")
    public void valueOfValidStrArgTest() {
        String str = "test";
        String str1 = FieldValueUtils.valueOfStr("test");
        assertEquals(str, str1, "Filtered string doesn't match");
    }

    @Test
    @DisplayName("Filter test with invalid string argument")
    public void valueOfInvalidStrArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldValueUtils.valueOfStr("!@#%"),
                "Exception is not thrown with invalid argument");
    }

    @Test
    @DisplayName("Boolean filter correct input test")
    public void valueOfValidBoolArgTest() {
        assertAll(() -> assertEquals(true, FieldValueUtils.valueOfBool("yEs"),
                "Correct input returns false or throws exception"),
                () -> assertEquals(true, FieldValueUtils.valueOfBool("y"),
                        "Correct input returns false or throws exception"),
                () -> assertEquals(false, FieldValueUtils.valueOfBool("nO"),
                        "Correct input returns true or throws exception"),
                () -> assertEquals(false, FieldValueUtils.valueOfBool("n"),
                        "Correct input returns true or throws exception"));
    }

    @Test
    @DisplayName("Boolean filter incorrect input test")
    public void valueOfInvalidBoolArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldValueUtils.valueOfBool("123"),
                "Invalid input");
    }

    @Test
    @DisplayName("Integer filter correct input test")
    public void valueOfValidIntArgTest() {
        assertAll(() -> assertEquals(new Integer(1), FieldValueUtils.valueOfInt("1"),
                "Correct input returns different number or throws exception"),
                () -> assertEquals(new Integer(11), FieldValueUtils.valueOfInt("11"),
                        "Correct input returns different number or throws exception"),
                () -> assertEquals(new Integer(123), FieldValueUtils.valueOfInt("123"),
                        "Correct input returns different number or throws exception"));
    }

    @Test
    @DisplayName("Integer filter incorrect input test")
    public void valueOfInvalidIntArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldValueUtils.valueOfInt("1235asdf"),
                "Invalid number");
    }

    @Test
    @DisplayName("Repeatable filter correct input test")
    public void valueOfValidRepeatableArgTest() {
        assertAll(() -> assertEquals(Event.RepeatableType.NONE, FieldValueUtils.valueOfRepeatable("n"),
                "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.NONE, FieldValueUtils.valueOfRepeatable("nOne"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.DAILY, FieldValueUtils.valueOfRepeatable("d"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.DAILY, FieldValueUtils.valueOfRepeatable("daily"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.WEEKLY, FieldValueUtils.valueOfRepeatable("w"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.WEEKLY, FieldValueUtils.valueOfRepeatable("weekly"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.MONTHLY, FieldValueUtils.valueOfRepeatable("m"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.MONTHLY, FieldValueUtils.valueOfRepeatable("monthly"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.YEARLY, FieldValueUtils.valueOfRepeatable("y"),
                        "Correct input returns different value or throws exception"),
                () -> assertEquals(Event.RepeatableType.YEARLY, FieldValueUtils.valueOfRepeatable("yearly"),
                        "Correct input returns different value or throws exception"));
    }

    @Test
    @DisplayName("Repeatable filter incorrect input test")
    public void valueOfInvalidRepeatableArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> FieldValueUtils.valueOfRepeatable("123"),
                "Invalid input");
    }
}
