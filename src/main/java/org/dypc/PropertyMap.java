package org.dypc;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PropertyMap {
    private int afterValueSpaces;
    private String comment;
    private Map<String, PropertyMap> propertyMap;
    private int leadingSpacesNum;
    private String key;
    private int beforeVlaueSpaces;
    private Object value;
    private String line;

    private static final Pattern pattern = Pattern.compile("(\\s*)(.*):(\\s+)(.*)(\\s*)#(.*)");

    public PropertyMap() {
        this.propertyMap = new HashMap<>();
    }

    public PropertyMap(int leadingSpacesNum, String key, int beforeVlaueSpaces, Object value, int afterValueSpaces, String line, String comment) {
        this.leadingSpacesNum = leadingSpacesNum;
        this.key = key;
        this.beforeVlaueSpaces = beforeVlaueSpaces;
        this.value = value;
        this.afterValueSpaces = afterValueSpaces;
        this.line = line;
        this.comment = comment;
        this.propertyMap = new HashMap<>();
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String getLine() {
        return line;
    }

    public PropertyMap addProperty(String line) {
        PropertyMap child = null;
        int colonIndex = line.indexOf(":");
        if (colonIndex != -1) {
            String key = line.substring(0, colonIndex);
            int leadingSpacesNum = key.indexOf(key.trim());
            key = key.trim();
            String valueAndComment = line.substring(colonIndex, line.length());
            int beforeVlaueSpaces = valueAndComment.indexOf(valueAndComment.trim());
            int numSignIndex = valueAndComment.indexOf(" #");
            int afterValueSpaces;
            String value;
            String comment;
            if (numSignIndex != -1) {
                value = valueAndComment.substring(0, numSignIndex - 1);
                comment = valueAndComment.substring(numSignIndex, valueAndComment.length() - 1);
                afterValueSpaces = valueAndComment.length() - value.length() - comment.length();
            } else {
                comment = "";
                value = valueAndComment.trim();
                afterValueSpaces = valueAndComment.length() - value.length() - beforeVlaueSpaces;
            }
            child = new PropertyMap(leadingSpacesNum, key, beforeVlaueSpaces, value, afterValueSpaces, line, comment);
            propertyMap.put(key, child);
        }
        return child ;
    }

    public PropertyMap getProperty(String key) {
        return propertyMap.get(key);
    }
}
