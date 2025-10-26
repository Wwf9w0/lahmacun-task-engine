package org.virtual.dag;


import org.virtual.model.VirtualLahmacunTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class FlowGraph {
    private final Map<String, FlowNode<?>> nodes = new LinkedHashMap<>();

    public <T> FlowNode<T> addNode(String id, Supplier<VirtualLahmacunTask<T>> task) {
        FlowNode<T> node = new FlowNode<>(id, task);
        nodes.put(id, node);
        return node;
    }

    public void addEdge(String fromId, String toId) {
        FlowNode<?> from = nodes.get(fromId);
        FlowNode<?> to = nodes.get(toId);
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid edge: " + fromId + " -> " + toId);
        }
        from.addNextNode(to);
    }

    public Collection<FlowNode<?>> getNodes() {
        return nodes.values();
    }

    public List<FlowNode<?>> getRoots() {
        List<FlowNode<?>> roots = new ArrayList<>();
        for (FlowNode<?> node : nodes.values()) {
            if (node.getPrevNodes().isEmpty()) {
                roots.add(node);
            }
        }
        return roots;
    }

    public void validate() {
        if (hasCycle()) {
            throw new IllegalStateException("FlowGraph contains a cycle!");
        }
    }

    private boolean hasCycle() {
        Set<FlowNode<?>> visited = new HashSet<>();
        Set<FlowNode<?>> stack = new HashSet<>();
        for (FlowNode<?> node : nodes.values()) {
            if (dfsCycle(node, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfsCycle(FlowNode<?> node, Set<FlowNode<?>> visited, Set<FlowNode<?>> stack) {
        if (stack.contains(node)) {
            return true;
        }
        if (visited.contains(node)) {
            return false;
        }
        visited.add(node);
        stack.add(node);
        for (FlowNode<?> next : node.getNextNodes()) {
            if (dfsCycle(next, visited, stack)) {
                return true;
            }
        }
        stack.remove(node);
        return false;
    }
}
