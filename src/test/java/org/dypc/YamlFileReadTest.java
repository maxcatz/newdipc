package org.dypc;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class YamlFileReadTest {

    @Test
    public void read() {
        PropertyConductor conductor = new PropertyConductor("src/test/resources/test.yaml");
        assertEquals("[one,two,three]", conductor.getPropertyValue("d.e"));
        System.out.println("Stop ");
    }

    @Test
    public void update() {
        PropertyConductor conductor = new PropertyConductor("src/test/resources/test.yaml");
        assertEquals("one", conductor.getPropertyValue("a.b.c"));
        Result result = conductor.setProperty("a.b.c", "two");
        assertEquals("a.b.c", result.getKey());
        assertEquals("one", result.getOldValue());
        assertEquals("two", result.getNewValue());
        assertEquals(Operation.MODIFY, result.getOperation());
        assertEquals("two", conductor.getPropertyValue("a.b.c"));
    }

    @Test
    public void addOneLevel() {
        PropertyConductor conductor = new PropertyConductor("src/test/resources/test.yaml");
        assertNull(conductor.getPropertyValue("a.b.z"));

        Result result = conductor.setProperty("a.b.z", "new value");
        assertEquals("a.b.z", result.getKey());
        assertEquals(null, result.getOldValue());
        assertEquals("new value", result.getNewValue());
        assertEquals(Operation.ADD, result.getOperation());

        result = conductor.setProperty("a.b.c.z", "plot twist");
        assertEquals("a.b.c.z", result.getKey());
        assertEquals(null, result.getOldValue());
        assertEquals(null, result.getNewValue());
        assertEquals(Operation.NONE, result.getOperation());

        result = conductor.setProperty("a.z", "some z value");
        assertEquals("a.z", result.getKey());
        assertEquals(null, result.getOldValue());
        assertEquals("some z value", result.getNewValue());
        assertEquals(Operation.ADD, result.getOperation());

        result = conductor.setProperty("f", "next value");
        assertEquals("f", result.getKey());
        assertEquals(null, result.getOldValue());
        assertEquals("next value", result.getNewValue());
        assertEquals(Operation.ADD, result.getOperation());

        assertEquals("new value", conductor.getPropertyValue("a.b.z"));
        assertEquals("some z value", conductor.getPropertyValue("a.z"));
        assertEquals("next value", conductor.getPropertyValue("f"));
        assertNull(conductor.getPropertyValue("a.b.c.z"));
    }

    @Test
    public void addSeveralLevels() {
        PropertyConductor conductor = new PropertyConductor("src/test/resources/test.yaml");
        Result result = conductor.setProperty("a.b.x.y.z", "two");
        assertEquals("a.b.x.y.z", result.getKey());
        assertEquals(null, result.getOldValue());
        assertEquals("two", result.getNewValue());
        assertEquals(Operation.ADD, result.getOperation());
        assertEquals("two", conductor.getPropertyValue("a.b.x.y.z"));
    }
}
