package org.engine.core;

import org.engine.dag.FlowGraph;
import org.engine.dag.FlowNode;
import org.engine.virtual.model.TaskLahmacunResult;
import org.engine.virtual.model.VirtualLahmacunTask;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;


public class FlowExecutorWithChef {

    private final FlowGraph graph;
    private final VirtuaLahmacunlChef chef;

    public FlowExecutorWithChef(FlowGraph graph, VirtuaLahmacunlChef chef) {
        this.graph = graph;
        this.chef = chef;
    }

    public Map<String, TaskLahmacunResult<?>> execute() {
        Map<String, TaskLahmacunResult<?>> results = new LinkedHashMap<>();
        Set<FlowNode<?>> executed = new HashSet<>();

        Queue<FlowNode<?>> queue = new ConcurrentLinkedDeque<>(graph.getRoots());

        while (!queue.isEmpty()) {
            // TODO need refactor to FlowNode list
            FlowNode<?> node = queue.poll();

            boolean ready = executed.containsAll(node.getPrevNodes());
            if (!ready) {
                queue.offer(node);
                continue;
            }

            VirtualLahmacunTask<?> task = new VirtualLahmacunTask<>(node.getTask());
            List<TaskLahmacunResult<?>> taskResults = chef.run(Collections.singletonList(task));
            TaskLahmacunResult<?> result = taskResults.get(0);

            System.out.println("Executed node: " + node.getId() + " -> " + result.status());

            results.put(node.getId(), result);
            executed.add(node);
            if (!node.getNextNodes().isEmpty()) {
                queue.addAll(node.getNextNodes());
            }
            graph.getRoots().remove(node);
        }
        return results;
    }
}
