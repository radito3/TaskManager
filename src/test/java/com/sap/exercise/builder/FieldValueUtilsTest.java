package com.sap.exercise.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
