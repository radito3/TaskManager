package com.sap.exercise.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StringFilterTest {

    @Test
    @DisplayName("Filter test with valid argument")
    public void filterValidArgTest() {
        String str = "test";
        String str1 = new StringFilter().valueOf("test");
        assertEquals(str, str1, "Filtered string doesn't match");
    }

    @Test
    @DisplayName("Filter test with invalid argument")
    public void filterInvalidArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> new StringFilter().valueOf("!@#%"),
                "Exception is not thrown with invalid argument");
    }
}
