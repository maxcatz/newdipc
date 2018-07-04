package org.dypc;

import org.junit.Assert;
import org.junit.Test;

import static org.dypc.PropertyConductor.BEFORE_KEY_INDEX;
import static org.dypc.PropertyConductor.BEFORE_VALUE_INDEX;
import static org.dypc.PropertyConductor.KEY_INDEX;
import static org.dypc.PropertyConductor.LINE_TAIL_INDEX;
import static org.dypc.PropertyConductor.VALUE_INDEX;

public class YamlReadTest {
    // test case
    @Test
    public void testCase(){
        validator("", "", "", "","", "");
    }
    // leaf cases
    @Test
    public void leafCases(){
        validator("key: ",                      "", "key", "","", " ");
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

        String[] tokens = PropertyConductor.parse(input);
        Assert.assertEquals(beforeKey, tokens[BEFORE_KEY_INDEX]);
        Assert.assertEquals(key, tokens[KEY_INDEX]);
        Assert.assertEquals(beforeValue, tokens[BEFORE_VALUE_INDEX]);
        Assert.assertEquals(Value, tokens[VALUE_INDEX] );
        Assert.assertEquals(afterValue, tokens[LINE_TAIL_INDEX]);
    }
}
