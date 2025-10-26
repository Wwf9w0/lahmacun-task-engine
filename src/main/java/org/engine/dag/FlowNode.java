package org.engine.dag;

import java.util.ArrayList;
import java.util.List;

public class FlowNode {

    private final String id;
    private final Runnable task;
    private final List<FlowNode> nextNodes = new ArrayList<>();
    private final List<FlowNode> prevNodes = new ArrayList<>();

    public FlowNode(String id, Runnable task) {
        this.id = id;
        this.task = task;
    }

    public void addNextNode(FlowNode next) {
        nextNodes.add(next);
        next.addPrevNode(this);
    }

    private void addPrevNode(FlowNode prev) {
        prevNodes.add(prev);
    }

    public String getId() {
        return id;
    }

    public List<FlowNode> getNextNodes() {
        return nextNodes;
    }

    public List<FlowNode> getPrevNodes() {
        return prevNodes;
    }

    public Runnable getTask() {
        return task;
    }

    @Override
    public String toString() {
        List<String> nextNames = nextNodes.stream().map(FlowNode::getId).toList();
        return "FlowNode{id='" + id + "', next=" + nextNames + "}";
    }
}
