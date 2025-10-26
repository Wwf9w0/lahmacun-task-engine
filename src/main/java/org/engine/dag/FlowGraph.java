package org.engine.dag;

import java.util.*;

public class FlowGraph {

    private final Map<String, FlowNode> nodes = new LinkedHashMap<>();

    public FlowNode addNode(String name, Runnable task) {
        FlowNode node = new FlowNode(name, task);
        nodes.put(name, node);
        return node;
    }

    public void addEdge(String fromId, String toId) {
        FlowNode from = nodes.get(fromId);
        FlowNode to = nodes.get(toId);
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid edge: " + fromId + " -> " + toId);
        }
        from.addNextNode(to);
    }

    public List<FlowNode> getRoots() {
        return nodes.values().stream()
                .filter(n -> n.getPrevNodes().isEmpty())
                .toList();
    }

    public Collection<FlowNode> getNodes() {
        return nodes.values();
    }

    public void validate() {
        if (hasCycle()) {
            throw new IllegalStateException("FlowGraph contains a cycle!");
        }
    }

    private boolean hasCycle() {
        Set<FlowNode> visited = new HashSet<>();
        Set<FlowNode> stack = new HashSet<>();
        for (FlowNode node : nodes.values()) {
            if (dfsCycle(node, visited, stack)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfsCycle(FlowNode node, Set<FlowNode> visited, Set<FlowNode> stack) {
        if (stack.contains(node)){
            return true;
        }
        if (visited.contains(node)){
            return false;
        }

        visited.add(node);
        stack.add(node);

        for (FlowNode next : node.getNextNodes()) {
            if (dfsCycle(next, visited, stack)) return true;
        }

        stack.remove(node);
        return false;
    }
}
