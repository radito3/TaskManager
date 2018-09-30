package com.sap.exercise.builder;

import com.sap.exercise.model.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventBuilderTest {

    @Test
    @DisplayName("Get fields test")
    public void getFieldsTest() {
        List<String> expected = Arrays.asList("Title", "When", "All day? [Y]es [N]o",
                "Repeat? [N]o [D]aily [W]eekly [M]onthly [Y]early");
        List<String> result = AbstractBuilder.getEventBuilder(new Event("", Event.EventType.REMINDER)).getFields();
        assertEquals(4, Stream.concat(expected.stream(), result.stream()).distinct().count());
    }

    //TODO add rest of event builder functionality tests
}
