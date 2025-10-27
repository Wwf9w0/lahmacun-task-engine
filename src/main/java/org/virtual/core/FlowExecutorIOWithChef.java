package org.virtual.core;

import org.virtual.dag.FlowGraph;
import org.virtual.dag.FlowNode;
import org.virtual.model.LahmacunTaskStatus;
import org.virtual.model.TaskLahmacunResult;
import org.virtual.queue.QueueManager;
import org.virtual.queue.QueuedTask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FlowExecutorIOWithChef<T> {

    private final VirtualThreadOven oven;
    private final QueueManager<T> queueManager;

    public FlowExecutorIOWithChef(FlowGraph graph, VirtualThreadOven oven, QueueManager<T> queueManager) {
        this.oven = oven;
        this.queueManager = queueManager;
    }

    public Map<String, TaskLahmacunResult<?>> execute(int process) {
        Map<String, CompletableFuture<TaskLahmacunResult<?>>> futures = new LinkedHashMap<>();
        for (QueuedTask<T> task : queueManager.pollAll(process)) {
            submitNode(task.getNode(), futures);
        }
        oven.waitAll(futures.values().toArray(new CompletableFuture[0]));
        Map<String, TaskLahmacunResult<?>> results = new LinkedHashMap<>();
        futures.forEach((nodeId, f) -> results.put(nodeId, f.join()));
        return results;
    }

    private void submitNode(FlowNode<?> node, Map<String, CompletableFuture<TaskLahmacunResult<?>>> futures) {
        Assert.nonNull(node, "node must not be null");
        Assert.nonNull(futures, "futures must not be null");
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
                .thenCompose(ignored -> {
                 return   node.getTask().get().run(oven)
                            .thenApply(result -> new TaskLahmacunResult<>(
                                    node.getId(),
                                    LahmacunTaskStatus.SUCCESS,
                                    result,
                                    null,
                                    0));
                });
        futures.put(node.getId(), future);
    }
}
