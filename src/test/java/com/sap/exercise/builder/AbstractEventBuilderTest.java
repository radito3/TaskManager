package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractEventBuilderTest {

    @Test
    @DisplayName("Event handler instance test")
    public void getEventHandlerTest() {
        assertTrue(AbstractEventBuilder.getEventBuilder(new Event("", Event.EventType.TASK)) instanceof TaskBuilder);
        assertTrue(AbstractEventBuilder.getEventBuilder(new Event("", Event.EventType.GOAL)) instanceof GoalBuilder);
        assertTrue(AbstractEventBuilder.getEventBuilder(new Event("", Event.EventType.REMINDER)) instanceof ReminderBuilder);
    }
}
