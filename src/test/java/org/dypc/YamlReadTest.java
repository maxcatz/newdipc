package org.dypc;

import org.junit.Assert;
import org.junit.Test;

public class YamlReadTest {


    @Test
    public void simpleLeaf(){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty("keyA: valueB");
        String[] tokens= child.getTokens();
        Assert.assertEquals("keyA", tokens[PropertyMap.KEY_INDEX]);
        Assert.assertEquals("valueB", tokens[PropertyMap.VALUE_INDEX] );
    }

    @Test
    public void leafWithComment(){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty("one: two        #Comment");
        String[] tokens= child.getTokens();
        Assert.assertEquals("one", tokens[PropertyMap.KEY_INDEX]);
        Assert.assertEquals("two", tokens[PropertyMap.VALUE_INDEX] );
        Assert.assertEquals("        #Comment", tokens[PropertyMap.AFTER_VALUE_INDEX]);
    }

    @Test
    public void chieldLeafWithComment(){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty("    one: two #Comment");
        String[] tokens= child.getTokens();
        Assert.assertEquals("    ", tokens[PropertyMap.BEFORE_KEY_INDEX]);
        Assert.assertEquals("one", tokens[PropertyMap.KEY_INDEX]);
        Assert.assertEquals("two", tokens[PropertyMap.VALUE_INDEX] );
        Assert.assertEquals(" #Comment", tokens[PropertyMap.AFTER_VALUE_INDEX]);
    }

    @Test
    public void emptyLine(){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty("");
        Assert.assertNull(child);
    }

    @Test
    public void keyOnly(){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty("     keyA:        ");
        String[] tokens= child.getTokens();
        Assert.assertEquals("     ", tokens[PropertyMap.BEFORE_KEY_INDEX]);
        Assert.assertEquals("keyA", tokens[PropertyMap.KEY_INDEX]);
        Assert.assertEquals("        ", tokens[PropertyMap.AFTER_VALUE_INDEX]);
    }

    @Test
    public void commentOnly(){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty("     #comment   now        ");
        String[] tokens= child.getTokens();
        Assert.assertEquals("     ", tokens[PropertyMap.BEFORE_KEY_INDEX]);
        Assert.assertEquals("keyA", tokens[PropertyMap.KEY_INDEX]);
        Assert.assertEquals("        ", tokens[PropertyMap.AFTER_VALUE_INDEX]);
    }
}
