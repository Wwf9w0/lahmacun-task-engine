package org.engine.virtual.model;

public final class TaskLahmacunResult<T> {
    private final String name;
    private final LahmacunTaskStatus status;
    private final T result;
    private final Throwable throwable;
    private final long durationMilis;

    public TaskLahmacunResult(String name, LahmacunTaskStatus status, T result, Throwable throwable, long durationMillis) {
        this.name = name;
        this.status = status;
        this.result = result;
        this.throwable = throwable;
        this.durationMilis = durationMillis;
    }

    public String name() {
        return name;
    }

    public LahmacunTaskStatus status() {
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
