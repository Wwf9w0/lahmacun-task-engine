package org.engine.scheduler;


public class SchedulerBuilder<T> {
    private String key;
    private String cron;
    private T data;
    private long timeoutMillis;

    public SchedulerBuilder(String key, String cron, T data, long timeoutMillis) {
        this.key = key;
        this.cron = cron;
        this.data = data;
        this.timeoutMillis = timeoutMillis;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public String toString() {
        return "SchedulerBuilder{" +
                "key='" + key + '\'' +
                ", cron='" + cron + '\'' +
                ", data=" + data +
                ", timeoutMillis=" + timeoutMillis +
                '}';
    }
}
