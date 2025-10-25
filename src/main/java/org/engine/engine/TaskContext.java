package org.engine.engine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskContext {

    private final Map<String, Object> results = new ConcurrentHashMap<>();


    public <T> void putResult(String taskName, T result) {
        results.put(taskName, result);
    }


    @SuppressWarnings("unchecked")
    public <T> T getResult(String taskName) {
        return (T) results.get(taskName);
    }

}