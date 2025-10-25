package org.engine.task.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Scheduler {
    private String cron;
    private int id;
    private String operatorType;
    private Map<String, Object> config;
    private List<String> upstreamTaskIds;
    private long timeoutMillis;

    public Scheduler(String cron, String operatorType, Map<String, Object> config) {
        this.cron = cron;
        this.id = 0;
        this.operatorType = operatorType;
        this.config = config;
        this.upstreamTaskIds = new ArrayList<>();
        this.timeoutMillis = 0L;
    }


    public void updateTimeout(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperatorType() {
        return operatorType;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }

    public List<String> getUpstreamTaskIds() {
        return upstreamTaskIds;
    }

    public void setUpstreamTaskIds(List<String> upstreamTaskIds) {
        this.upstreamTaskIds = upstreamTaskIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Scheduler{" +
                "cron='" + cron + '\'' +
                ", id=" + id +
                ", operatorType='" + operatorType + '\'' +
                ", config=" + config +
                ", upstreamTaskIds=" + upstreamTaskIds +
                ", timeoutMillis=" + timeoutMillis +
                '}';
    }
}
