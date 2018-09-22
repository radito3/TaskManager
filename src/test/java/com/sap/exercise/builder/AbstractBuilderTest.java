package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractBuilderTest {

    @Test
    @DisplayName("Filter test with valid argument")
    public void filterValidArgTest() {
        String str = "test";
        String str1 = AbstractBuilder.filterInput("test", string -> string.matches("[-_.a-zA-Z0-9]+"),
                IllegalArgumentException::new);
        assertEquals(str, str1, "Filtered string doesn't match");
    }

    @Test
    @DisplayName("Filter test with invalid argument")
    public void filterInvalidArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> AbstractBuilder.filterInput("!@#%", string -> string.matches("[-_.a-zA-Z0-9]+"),
                        IllegalArgumentException::new),
                "Exception is not thrown with invalid argument");
    }

    @Test
    @DisplayName("Filter test with null argument")
    public void filterNullArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> AbstractBuilder.filterInput("", string -> string.matches("[-_.a-zA-Z0-9]+"),
                        IllegalArgumentException::new),
                "Exception is not thrown with null argument");
    }

    @Test
    @DisplayName("Event handler instance test")
    public void getEventHandlerTest() {
        assertTrue(AbstractBuilder.getEventBuilder(new Event("", Event.EventType.GOAL)) instanceof GoalBuilder);
        assertTrue(AbstractBuilder.getEventBuilder(new Event("", Event.EventType.REMINDER)) instanceof ReminderBuilder);
        assertTrue(AbstractBuilder.getEventBuilder(new Event("", Event.EventType.TASK)) instanceof TaskBuilder);
    }

    @Test
    @DisplayName("Get fields test")
    public void getFieldsTest() {
        List<String> expected = Arrays.asList("Title", "When", "All day? [Y]es [N]o",
                "Repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early");
        List<String> result = new ReminderBuilder(new Event()).getFields();
        assertTrue(Stream.concat(expected.stream(), result.stream()).distinct().count() == 4);
    }
}
