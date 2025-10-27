package org.virtual.queue;

import org.virtual.core.VirtualLahmacunTask;
import org.virtual.dag.FlowNode;
import org.virtual.model.TaskType;

public class QueuedTask<T> {
    private FlowNode<?> node;
    private  TaskType type;
    private  String nodeId;
    private  long submitTime;
    private  int priority;

    public QueuedTask(FlowNode<?>  node, TaskType type,String nodeId, long submitTime,  int priority) {
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

    public FlowNode<?>  getNode() {
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
