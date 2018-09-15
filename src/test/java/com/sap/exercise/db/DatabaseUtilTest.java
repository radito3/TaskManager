package com.sap.exercise.db;

import com.sap.exercise.model.BaseEvent;
import com.sap.exercise.model.Task;
import org.hibernate.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUtilTest {

    @Test
    @DisplayName("Db modification test")
    public void processObjectTest() {
        DatabaseUtil client = new DatabaseUtil();
        Task task = new Task();
        client.processObject((Session s) -> s.save(task));
        assertAll(() -> {
            assertNotNull(client.getObject(s -> s.get(Task.class, task.getId())));
            assertEquals(client.getObject(s -> s.get(Task.class, task.getId())), task);
        });
    }

    @Test
    @DisplayName("Getting invalid object from db test")
    public void getInvalidObjectTest() {
        DatabaseUtil client = new DatabaseUtil();
        assertAll(() -> {
            assertThrows(IllegalStateException.class, () -> client.getObject(s -> s.get(BaseEvent.class, 0)));
            assertThrows(NullPointerException.class, () -> client.getObject(s -> s.get(Task.class, -1)));
        });
    }
}
