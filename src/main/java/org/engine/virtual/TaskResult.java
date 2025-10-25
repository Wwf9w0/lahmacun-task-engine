package org.engine.virtual;

public final class TaskResult<T> {
    private final String name;
    private final TaskStatus status;
    private final T result;
    private final Throwable throwable;
    private final long durationMilis;

    public TaskResult(String name, TaskStatus status, T result, Throwable throwable, long durationMillis) {
        this.name = name;
        this.status = status;
        this.result = result;
        this.throwable = throwable;
        this.durationMilis = durationMillis;
    }

    public String name() {
        return name;
    }

    public TaskStatus status() {
        return status;
    }

    public T result() {
        return result;
    }

    public Throwable error() {
        return throwable;
    }

    public long durationMillis() {
        return durationMilis;
    }

}
