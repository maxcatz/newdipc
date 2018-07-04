package org.dypc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class PropertyConductor {

    private PropertyMap propertyMap;
    private List<PropertyMap> propertyList;

    public PropertyConductor( String fileName) {
        PropertyMap propertyMap = new PropertyMap();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> {
                propertyMap.addProperty(line);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T getPropertyValue (String key){
         return (T) propertyMap.getProperty(key).getTokens()[PropertyMap.VALUE_INDEX];
     }

    public <T> Result setProperty (String key, T value){
        return setProperty(key,value,Operation.ADD_OR_MODIFY);
    }

    public <T> Result setProperty (String key, T value, Operation operation){
        return null;
    }
}
