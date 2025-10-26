package org.virtual.core;

import org.virtual.dag.FlowGraph;
import org.virtual.dag.FlowNode;
import org.virtual.model.TaskLahmacunResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FlowExecutorWithChef {

    private final FlowGraph graph;
    private final VirtualLahmacunChef chef;

    public FlowExecutorWithChef(FlowGraph graph, VirtualLahmacunChef chef) {
        this.graph = graph;
        this.chef = chef;
    }

    public Map<String, TaskLahmacunResult<?>> execute() {
        Map<String, CompletableFuture<TaskLahmacunResult<?>>> futures = new LinkedHashMap<>();
        for (FlowNode node : graph.getNodes()) {
            submitNode(node, futures);
        }
        chef.waitAll(futures.values().toArray(new CompletableFuture[0]));
        Map<String, TaskLahmacunResult<?>> results = new LinkedHashMap<>();
        futures.forEach((nodeId, f) -> results.put(nodeId, f.join()));
        return results;
    }

    private void submitNode(FlowNode<?> node, Map<String, CompletableFuture<TaskLahmacunResult<?>>> futures) {
        if (futures.containsKey(node.getId())) {
            return;
        }
        List<CompletableFuture<TaskLahmacunResult<?>>> depFutures = new ArrayList<>();
        for (FlowNode<?> prev : node.getPrevNodes()) {
            submitNode(prev, futures);
            depFutures.add(futures.get(prev.getId()));
        }
        CompletableFuture<TaskLahmacunResult<?>> future = CompletableFuture
                .allOf(depFutures.toArray(new CompletableFuture[0]))
                .thenCompose(ignored -> chef.oven().submit(() -> {
                    VirtualLahmacunTask<?> task = (VirtualLahmacunTask<?>) node.getTask().get();
                    List<TaskLahmacunResult<?>> taskResults = chef.run(Collections.singletonList(task));
                    return taskResults.getFirst();
                }));
        futures.put(node.getId(), future);
    }
}
