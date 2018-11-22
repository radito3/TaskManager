package com.sap.exercise.wrapper;

import com.sap.exercise.model.Event;
import com.sap.exercise.wrapper.fields.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventWrapperTest {

    @Test
    @DisplayName("Get fields for task test")
    public void getTaskFieldsTest() {
        Event task = new Event("", Event.EventType.TASK);

        EventWrapper taskWrapper = new EventWrapper(task);

        List<FieldInfo> taskFields = Arrays.asList(new TitleFieldInfo(task), new TimeOfFieldInfo(task), new AllDayFieldInfo(task),
                new LocationFieldInfo(task), new DescriptionFieldInfo(task), new ToRepeatFieldInfo(task, false),
                new ReminderFieldInfo(task), new DurationFieldInfo(task, false));

        assertTrue(taskWrapper.getFields().containsAll(taskFields),
                "Event wrapper for task doesn't contain all of the fields for a task");
    }

    @Test
    @DisplayName("Ge fields for reminder test")
    public void getReminderFieldsTest() {
        Event reminder = new Event("", Event.EventType.REMINDER);

        EventWrapper reminderWrapper = new EventWrapper(reminder);

        List<FieldInfo> reminderFields = Arrays.asList(new TitleFieldInfo(reminder), new TimeOfFieldInfo(reminder), new AllDayFieldInfo(reminder),
                new ToRepeatFieldInfo(reminder, false));

        assertTrue(reminderWrapper.getFields().containsAll(reminderFields),
                "Event wrapper for reminder doesn't contain all of the fields for a reminder");
    }

    @Test
    @DisplayName("Ge fields for goal test")
    public void getGoalFieldsTest() {
        Event goal = new Event("", Event.EventType.GOAL);

        EventWrapper goalWrapper = new EventWrapper(goal);

        List<FieldInfo> goalFields = Arrays.asList(new TitleFieldInfo(goal), new TimeOfFieldInfo(goal), new ToRepeatFieldInfo(goal, true),
                new ReminderFieldInfo(goal), new DurationFieldInfo(goal, true));

        assertTrue(goalWrapper.getFields().containsAll(goalFields),
                "Event wrapper for goal doesn't contain all of the fields for a goal");
    }
}
