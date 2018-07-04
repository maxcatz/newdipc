package org.dypc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class YamlFileRead {

    @Test
    public void read(){
        PropertyConductor conductor = new PropertyConductor("src/test/resources/test.yaml");

        System.out.println("Stop ");
    }

    @Test
    public void update() {
        PropertyConductor conductor = new PropertyConductor("src/test/resources/test.yaml");
        assertEquals("one", conductor.getPropertyValue("a.b.c"));
        conductor.setProperty("a.b.c", "two");
        assertEquals("two", conductor.getPropertyValue("a.b.c"));
    }
}
