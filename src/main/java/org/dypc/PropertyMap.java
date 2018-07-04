package org.dypc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PropertyMap {
    private static final int BEFORE_KEY_INDEX = 0;
    private static final int KEY_INDEX = 1;
    private static final int BEFORE_VALUE_INDEX = 2;
    private static final int VALUE_INDEX = 3;
    private static final int AFTER_VALUE_INDEX = 4;

    private String[] tokens;
    private Map<String, PropertyMap> propertyMap;

    private static final Pattern pattern = Pattern.compile("(\\s*)(.*):(\\s+)(.*)(\\s*)#(.*)");

    public PropertyMap() {
        this.tokens = null;
        this.propertyMap = new HashMap<>();
    }

    public PropertyMap(String[] tokens) {
        this.tokens = tokens;
        this.propertyMap = new HashMap<>();
    }

    public PropertyMap addProperty(String line) {
        PropertyMap child = null;
        String[] childTokens = new String[5];
        int colonIndex = line.indexOf(":");
        if (colonIndex != -1) {
            childTokens[KEY_INDEX] = line.substring(0, colonIndex).trim();
            childTokens[BEFORE_KEY_INDEX] = line.substring(0, line.indexOf(childTokens[KEY_INDEX]));
            String valueAndComment = line.substring(colonIndex - 1);
            int numSignIndex = valueAndComment.indexOf(" #");
            childTokens[BEFORE_VALUE_INDEX] = valueAndComment.substring(0, valueAndComment.indexOf(childTokens[VALUE_INDEX]));
            if (numSignIndex != -1) {
                childTokens[VALUE_INDEX] = valueAndComment.substring(0, numSignIndex).trim();
                childTokens[AFTER_VALUE_INDEX] = valueAndComment.substring(numSignIndex - 1);

            } else {
                childTokens[VALUE_INDEX] = valueAndComment.trim();
                childTokens[AFTER_VALUE_INDEX] = "";
            }
            child = new PropertyMap(childTokens);
            propertyMap.put(childTokens[KEY_INDEX], child);
        }
        return child;
    }

    public PropertyMap getProperty(String key) {
        return propertyMap.get(key);
    }

    public String[] getTokens() {
        return tokens;
    }

    public String getLine() {
        return Arrays.stream(tokens).collect(Collectors.joining(""));
    }
}
