package com.sap.exercise.db;

import com.sap.exercise.AbstractTest;
import com.sap.exercise.model.AbstractModel;
import com.sap.exercise.model.Event;
import org.hibernate.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseUtilTest extends AbstractTest {

    @Test
    @DisplayName("Db modification test")
    public void processObjectTest() {
        DatabaseUtil client = new DatabaseUtil();
        Event event = new Event();
        client.processObject((Session s) -> s.save(event));
        assertAll("Retrieved object integrity assertions",
                () -> assertNotNull(client.getObject(s -> s.get(Event.class, event.getId())), "Object retrieved from db is null"),
                () -> assertEquals(client.getObject(s -> s.get(Event.class, event.getId())), event, "Object retrieved from db is incorrect")
        );
    }

    @Test
    @DisplayName("Getting invalid object from db test")
    public void getInvalidObjectTest() {
        DatabaseUtil client = new DatabaseUtil();
        assertAll("Exception throwing assertions",
                () -> assertThrows(IllegalStateException.class,
                        () -> client.getObject(s -> s.get(AbstractModel.class, 0)),
                        "No exception thrown in case of non-configured class"),
                () -> assertThrows(NullPointerException.class,
                        () -> client.getObject(s -> s.get(Event.class, -1)),
                        "No exception thrown in case of invalid Id argument")
        );
    }
}
