package org.dypc;

public class PropertyConductor {


    public PropertyConductor( String file) {
    }

    public <T> T getProperty (String key){
         return null;
     }

    public <T> Result setProperty (String key, T value){
        return setProperty(key,value,Operation.ADD_OR_MODIFY);
    }

    public <T> Result setProperty (String key, T value, Operation operation){
        return null;
    }
}
