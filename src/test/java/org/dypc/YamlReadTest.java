package org.dypc;

import org.junit.Assert;
import org.junit.Test;

public class YamlReadTest {
    // test case
    @Test
    public void testCase(){
        validator("", "", "", "","", "");
    }
    // leaf cases
    @Test
    public void leafCases(){
        validator("key: ",                      "", "key", " ","value", "");
        validator("key: value",                 "", "key", " ","value", "");
        validator("key: value #comment",        "", "key", " ","value", " #comment");
        validator("key: #comment",              "", "key", "","", " #comment");
    }

    // child cases
    @Test
    public void childCases(){
        validator(" key: value",                " ", "key", " ","value", "");
        validator(" key: value #comment",       " ", "key", " ","value", " #comment");
    }

    // varied cases
    @Test
    public void variedCases(){
        validator("", "", "", "","", "");
        validator("#comment", "", "", "","", "#comment");
        validator("  #comment  now  ", "", "", "","", "  #comment  now  ");
    }

    // method of validation
    public void validator(String input, String beforeKey, String key, String beforeValue, String Value, String afterValue){
        PropertyMap propertyMap = new PropertyMap();
        PropertyMap child = propertyMap.addProperty(input);
        String[] tokens= child.getTokens();
        Assert.assertEquals(beforeKey, tokens[PropertyMap.BEFORE_KEY_INDEX]);
        Assert.assertEquals(key, tokens[PropertyMap.KEY_INDEX]);
        Assert.assertEquals(beforeValue, tokens[PropertyMap.BEFORE_VALUE_INDEX]);
        Assert.assertEquals(Value, tokens[PropertyMap.VALUE_INDEX] );
        Assert.assertEquals(afterValue, tokens[PropertyMap.AFTER_VALUE_INDEX]);
    }
}
