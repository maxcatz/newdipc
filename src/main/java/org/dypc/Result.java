package org.dypc;

public class Result {
    private String key;
    private Object oldValue;
    private Object newValue;
    private Operation operation;

    public Result(String key, Object oldValue, Object newValue, Operation operation) {
        this.key = key;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.operation = operation;
    }

    public String getKey() {
        return key;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Operation getOperation() {
        return operation;
    }
}
