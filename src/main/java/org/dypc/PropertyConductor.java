package org.dypc;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;


public class PropertyConductor {

    public static final int BEFORE_KEY_INDEX = 0;
    public static final int KEY_INDEX = 1;
    public static final int BEFORE_VALUE_INDEX = 2;
    public static final int VALUE_INDEX = 3;
    public static final int LINE_TAIL_INDEX = 4;

    private List<Pair<String,String[]>> propertyList;

    public PropertyConductor( String fileName) {
        propertyList = new ArrayList<>();
        Stack<Pair<String, Integer>> levels = new Stack<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                String[] next = parse(line);
                if( next[KEY_INDEX].isEmpty() ) {
                    propertyList.add(Pair.of(next[KEY_INDEX], next));
                    return;
                }
                Pair<String, Integer> prevLevel = null;
                if( !levels.empty()) {
                    prevLevel = levels.peek();
                    while( prevLevel != null && prevLevel.getRight() >= next[BEFORE_KEY_INDEX].length()){
                        levels.pop();
                        if (!levels.isEmpty()) {
                            prevLevel = levels.peek();
                        } else {
                            prevLevel = null;
                        }
                    }
                }
                String key;
                if (!levels.isEmpty()) {
                    key = prevLevel.getLeft() + "." + next[KEY_INDEX];
                } else {
                    key = next[KEY_INDEX];
                }
                levels.push(Pair.of(key, next[BEFORE_KEY_INDEX].length()));
                propertyList.add(Pair.of(key, next));
//                int place = findPlace(key);
//                if (place == -1) {
//                    propertyList.add(Pair.of(key, next));
//                } else {
//                    propertyList.add(place, Pair.of(key, next));
//                }
                System.out.println(key + ":" + next[VALUE_INDEX]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyValue (String key){
        int place = findPlace(key);
        Pair<String, String[]> property = propertyList.get(place);
        if (property.getLeft().equals(key)) {
            return property.getValue()[VALUE_INDEX];
        }
        return null;
    }

    public Result setProperty (String key, String value){
        return setProperty(key,value,Operation.ADD_OR_MODIFY);
    }

    public  Result setProperty (String key, String value, Operation operation){
        int place = findPlace(key);
        if (place != -1) {
            Pair<String, String[]> property = propertyList.get(place);
            if (property.getLeft().equals(key) && !Operation.ADD.equals(operation)) {
                property.getValue()[VALUE_INDEX] = value;
            } else if (!Operation.MODIFY.equals(operation)) {
                propertyList.add(place, Pair.of(key, new String[]{"CHANGE THIS!!!!!!!", key, "", value, ""}));
            }
        } else {
            propertyList.add(Pair.of(key, new String[]{"", key, "", value, ""}));
        }
        return null;
    }

    public static String[] parse (String line) {
        String[] childTokens = new String[]{"","","","","",""};
        int colonIndex = line.indexOf(":");
        if (colonIndex != -1) {
            childTokens[KEY_INDEX] = line.substring(0, colonIndex).trim();
            childTokens[BEFORE_KEY_INDEX] = line.substring(0, line.indexOf(childTokens[KEY_INDEX]));
            String valueAndComment = line.substring(colonIndex + 1);
            int numSignIndex = valueAndComment.indexOf(" #");
            if (numSignIndex != -1) {
                childTokens[VALUE_INDEX] = valueAndComment.substring(0, numSignIndex).trim();
                childTokens[LINE_TAIL_INDEX] = valueAndComment.substring(numSignIndex + 1);

            } else {
                childTokens[VALUE_INDEX] = valueAndComment.trim();
            }
            childTokens[BEFORE_VALUE_INDEX] = valueAndComment.substring(0, valueAndComment.indexOf(childTokens[VALUE_INDEX]));
            childTokens[LINE_TAIL_INDEX] = valueAndComment.substring(valueAndComment.indexOf(childTokens[VALUE_INDEX]) + childTokens[VALUE_INDEX].length());
        } else {
            childTokens[LINE_TAIL_INDEX] = line;
        }
        return childTokens;
    }

    public int findPlace(String key) {
        for (int i = propertyList.size() - 1; i >= 0; i--) {
            Pair<String, String[]> current = propertyList.get(i);
            if (!current.getKey().isEmpty() && key.startsWith(current.getKey()))
                return i;
        }
        return -1;
    }
}
