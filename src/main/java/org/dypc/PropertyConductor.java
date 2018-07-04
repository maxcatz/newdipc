package org.dypc;

import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private String file;

    public PropertyConductor( String file) {
        this.file = file;
        propertyList = new ArrayList<>();
        Stack<Pair<String, Integer>> levels = new Stack<>();
        try (Stream<String> stream = Files.lines(Paths.get(file))) {
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
                System.out.println(key + ":" + next[VALUE_INDEX]);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropertyValue (String key){
        int place = findPlace(key);
        if (place == -2 || place == -1) {
            return null;
        }
        Pair<String, String[]> property = propertyList.get(place);
        if (property.getLeft().equals(key)) {
            return property.getValue()[VALUE_INDEX];
        }
        return null;
    }

    public Result setProperty (String key, String value){
        return setProperty(key,value,Operation.ADD_OR_MODIFY);
    }

    public  Result setProperty (String key, String value, Operation operation) {
        int place = findPlace(key);
        if (place == -2) {
            return new Result(key, null, null, Operation.NONE);
        }

        if (place == -1) {
            if (!Operation.MODIFY.equals(operation)) {
                propertyList.add(Pair.of(key, new String[]{propertyList.isEmpty() ? "" : propertyList.get(0).getRight()[BEFORE_KEY_INDEX], key, "", value, ""}));
                return new Result(key, null, value, Operation.ADD);
            } else {
                return new Result(key, null, null, Operation.NONE);
            }
        }

        Pair<String, String[]> property = propertyList.get(place);
        if (property.getLeft().equals(key)) {
            if (!Operation.ADD.equals(operation)) {
                String oldValue = propertyList.get(place).getRight()[VALUE_INDEX];
                property.getValue()[VALUE_INDEX] = value;
                return new Result(key, oldValue, value, Operation.MODIFY);
            } else {
                return new Result(key, null, null, Operation.NONE);
            }
        }

        if (!Operation.MODIFY.equals(operation)) {
            String beforeKeyIndention = getIndention(key, place);
            propertyList.add(place + 1, Pair.of(key, new String[]{beforeKeyIndention, key, "", value, ""}));
            return new Result(key, null, value, Operation.ADD);
        }
        return new Result(key, null, null, Operation.NONE);
    }

    private String getIndention(String key, int place) {
        int keyLevel = key.split("\\.").length;
        for (int i = place; i >= 0; i--) {
            int prevLevel = propertyList.get(i).getLeft().split("\\.").length;
            if (prevLevel == keyLevel) {
                return propertyList.get(i).getRight()[BEFORE_KEY_INDEX];
            } else if (prevLevel < keyLevel) {
                return propertyList.get(i).getRight()[BEFORE_KEY_INDEX] + "  ";
            }
        }
        return "";
    }

    protected static String[] parse (String line) {
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

    private int findPlace(String key) {
        for (int i = propertyList.size() - 1; i >= 0; i--) {
            Pair<String, String[]> current = propertyList.get(i);
            if (!current.getLeft().isEmpty() && key.startsWith(current.getLeft())) {
                if (key.equals(current.getLeft()) || current.getRight()[VALUE_INDEX].isEmpty()) {
                    return i;
                } else {
                    return -2;
                }
            }
        }
        return -1;
    }


    public void flash() throws IOException{
        File fout = new File(file);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < propertyList.size(); i++) {
            String[] tokens = propertyList.get(i).getRight();
            bw.write(tokens[BEFORE_KEY_INDEX]+tokens[KEY_INDEX]+(tokens[KEY_INDEX].isEmpty()?"":":")+tokens[BEFORE_VALUE_INDEX]+tokens[VALUE_INDEX]+tokens[LINE_TAIL_INDEX]);
            if(i != propertyList.size()-1)
                bw.newLine();
        }

        bw.close();
    }
}
