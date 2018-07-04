package org.dypc;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        Stack<Pair<String, Integer>> levels = new Stack<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                String[] next = parse(line);
                if( next[KEY_INDEX].isEmpty() )
                    return;
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
                System.out.println(key + ":" + next[VALUE_INDEX]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//
//    public <T> T getPropertyValue (String key){
//         return (T) propertyMap.getProperty(key).getTokens()[VALUE_INDEX];
//     }

    public <T> Result setProperty (String key, T value){
        return setProperty(key,value,Operation.ADD_OR_MODIFY);
    }

    public <T> Result setProperty (String key, T value, Operation operation){
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
}
