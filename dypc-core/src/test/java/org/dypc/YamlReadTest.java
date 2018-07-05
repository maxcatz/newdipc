package org.dypc;

import org.junit.Assert;
import org.junit.Test;

import static org.dypc.PropertyConductor.BEFORE_KEY_INDEX;
import static org.dypc.PropertyConductor.BEFORE_VALUE_INDEX;
import static org.dypc.PropertyConductor.KEY_INDEX;
import static org.dypc.PropertyConductor.LINE_TAIL_INDEX;
import static org.dypc.PropertyConductor.VALUE_INDEX;

public class YamlReadTest {
    // case template:
    @Test
    public void caseTemplate(){
        validator("", "", "", "","", "");
    }

    // simple leaf cases:
    @Test
    public void simpleLeafCases(){
        validator("key: ",                       "", "key", "","", " ");
        validator("key:    ",                    "", "key", "","", "    ");
        validator("key: value",                  "", "key", " ","value", "");
        validator("key: value   ",               "", "key", " ","value", "   ");
        validator("key:    value",               "", "key", "    ","value", "");
        validator("key: value #comment",         "", "key", " ","value", " #comment");
        validator("key:   value   #comment   ",  "", "key", "   ","value", "   #comment   ");
        validator("key: #comment",               "", "key", "","", " #comment");
        validator("key:   #comment   ",          "", "key", "","", "   #comment   ");
    }

    // special leaf cases:
    @Test
    public void specialLeafCases(){
        validator("123: ",                       "", "123", "","", " ");
        validator("a1!: ",                       "", "a1!", "","", " ");
        validator("2q@:    ",                    "", "2q@", "","", "    ");
        validator("~`#$%^&*()-_+: ",             "", "~`#$%^&*()-_+", "","", " ");
        //validator("=]}[{\":?'>|<: ",            "", "=]}[{\":?'>|<", "","", " ");
        validator("k e y: v a l u e",            "", "k e y", " ","v a l u e", "");
        validator("key: va#lue   ",              "", "key", " ","va#lue", "   ");
        validator("key:    va# lue",             "", "key", "    ","va# lue", "");
        //validator("ke:y: valu# e #comment",     "", "ke:y", " ","valu# e", " #comment");
        validator("key:   value   #comment   ",  "", "key", "   ","value", "   #comment   ");
        validator("key: #comment",               "", "key", "","", " #comment");
        validator("key:   #comment   ",          "", "key", "","", "   #comment   ");
    }

    // simple child cases:
    @Test
    public void simpleChildCases(){
        validator(" key: ",                      " ", "key", "","", " ");
        validator("  key:    ",                  "  ", "key", "","", "    ");
        validator(" key: value",                 " ", "key", " ","value", "");
        validator("   key: value   ",            "   ", "key", " ","value", "   ");
        validator("    key:    value",           "    ", "key", "    ","value", "");
        validator(" key: value #comment",        " ", "key", " ","value", " #comment");
        validator("      key: value #comment",   "      ", "key", " ","value", " #comment");
        validator(" key:   value   #comment   ", " ", "key", "   ","value", "   #comment   ");
        validator("   key:   value   #comment ", "   ", "key", "   ","value", "   #comment ");
        validator(" key: #comment",              " ", "key", "","", " #comment");
        validator("          key:   #comment   ","          ", "key", "","", "   #comment   ");
        validator(" key:            #comment   "," ", "key", "","", "            #comment   ");
    }

    // varied cases:
    @Test
    public void variedCases(){
        validator("",                            "", "","","", "");
        validator("#comment",                    "", "", "","", "#comment");
        validator("#comment ........",           "", "", "","", "#comment ........");
        validator("  #comment  now  !!!  ",      "", "", "","", "  #comment  now  !!!  ");
    }

    // method of validation
    public void validator(String input, String beforeKey, String key, String beforeValue, String Value, String lineTail){
        String[] tokens = PropertyConductor.parse(input);
        Assert.assertEquals(beforeKey, tokens[BEFORE_KEY_INDEX]);
        Assert.assertEquals(key, tokens[KEY_INDEX]);
        Assert.assertEquals(beforeValue, tokens[BEFORE_VALUE_INDEX]);
        Assert.assertEquals(Value, tokens[VALUE_INDEX] );
        Assert.assertEquals(lineTail, tokens[LINE_TAIL_INDEX]);
    }
}
