package org.virtual.dag;

import org.virtual.model.VirtualLahmacunTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FlowNode<T> {

    private final String id;
    private final Supplier<VirtualLahmacunTask<T>> task;
    private final List<FlowNode<?>> nextNodes = new ArrayList<>();
    private final List<FlowNode<?>> prevNodes = new ArrayList<>();

    public FlowNode(String id, Supplier<VirtualLahmacunTask<T>>  task) {
        this.id = id;
        this.task = task;
    }

    public String getId() { return id; }
    public Supplier<VirtualLahmacunTask<T>>  getTask() { return task; }
    public List<FlowNode<?>> getNextNodes() { return nextNodes; }
    public List<FlowNode<?>> getPrevNodes() { return prevNodes; }

    public void addNextNode(FlowNode<?> next) {
        nextNodes.add(next);
        next.prevNodes.add(this);
    }

    @Override
    public String toString() {
        return "FlowNode{id='" + id + "', next=" + nextNodes + "}";
    }
}
