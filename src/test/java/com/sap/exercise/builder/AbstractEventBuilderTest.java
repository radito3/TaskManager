package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractEventBuilderTest {

    @Test
    @DisplayName("Filter test with valid argument")
    @Disabled("Will be deleted")
    public void filterValidArgTest() {
        String str = "test";
        String str1 = AbstractEventBuilder.filterInput("test", string -> string.matches("[-_.a-zA-Z0-9]+"),
                IllegalArgumentException::new);
        assertEquals(str, str1, "Filtered string doesn't match");
    }

    @Test
    @DisplayName("Filter test with invalid argument")
    @Disabled("Will be deleted")
    public void filterInvalidArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> AbstractEventBuilder.filterInput("!@#%", string -> string.matches("[-_.a-zA-Z0-9]+"),
                        IllegalArgumentException::new),
                "Exception is not thrown with invalid argument");
    }

    @Test
    @DisplayName("Filter test with null argument")
    @Disabled("Will be deleted")
    public void filterNullArgTest() {
        assertThrows(IllegalArgumentException.class,
                () -> AbstractEventBuilder.filterInput("", string -> string.matches("[-_.a-zA-Z0-9]+"),
                        IllegalArgumentException::new),
                "Exception is not thrown with null argument");
    }

    @Test
    @DisplayName("Event handler instance test")
    public void getEventHandlerTest() {
        assertTrue(AbstractEventBuilder.getEventBuilder(new Event("", Event.EventType.TASK)) instanceof TaskBuilder);
        assertTrue(AbstractEventBuilder.getEventBuilder(new Event("", Event.EventType.GOAL)) instanceof GoalBuilder);
        assertTrue(AbstractEventBuilder.getEventBuilder(new Event("", Event.EventType.REMINDER)) instanceof ReminderBuilder);
    }
}
