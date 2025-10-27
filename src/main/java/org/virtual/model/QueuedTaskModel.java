package org.virtual.model;

import org.virtual.dag.FlowNode;

public class QueuedTaskModel<T> {

    private FlowNode<?> node;
    private TaskType type;
    private String nodeId;
    private long submitTime;
    private int priority;

    public QueuedTaskModel(FlowNode<?> node, TaskType type, String nodeId, long submitTime, int priority) {
        this.node = node;
        this.type = type;
        this.nodeId = nodeId;
        this.submitTime = submitTime;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public FlowNode<?> getNode() {
        return node;
    }

    public void setTask(FlowNode<?> node) {
        this.node = node;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
