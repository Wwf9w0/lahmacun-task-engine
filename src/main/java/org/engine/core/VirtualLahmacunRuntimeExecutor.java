package org.engine.core;

import org.engine.dag.FlowGraph;
import org.engine.dag.FlowNode;
import org.engine.model.QueuedTaskModel;
import org.engine.model.TaskLahmacunResult;
import org.engine.model.TaskType;
import org.engine.queue.QueueManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class VirtualLahmacunRuntimeExecutor<T> {

    private final QueueManager<T> queueManager;

    public VirtualLahmacunRuntimeExecutor(QueueManager<T> queueManager) {
        this.queueManager = queueManager;
    }

    public void startAllFlows(FlowGraph graph, int type) throws Exception {
        Assert.nonNull(graph, "FlowGraph must not be null");
        Map<String, TaskLahmacunResult<?>> allResults = new LinkedHashMap<>();
        dynamicRun(allResults, graph, type);
        System.out.println("\n--- VirtualFlow Runtime Results ---");
        allResults.forEach((id, r) -> System.out.println(id + " -> " + r.status() + " | " + r.result() + " | " + r.durationMillis()));
        AtomicLong sum = new AtomicLong();
        allResults.forEach((k, v) -> sum.addAndGet(v.durationMillis()));
        System.out.println("Completed Flows -> " + sum + " ms");
    }

    private Map<String, TaskLahmacunResult<?>> dynamicRun(Map<String, TaskLahmacunResult<?>> allResults, FlowGraph graph, int type) throws Exception {
        if (type == 1) {
            try (DedicatedPoolThreadOven ddOven = new DedicatedPoolThreadOven();) {
                FlowExecutorCPUWithChef<T> executorCPUWithChef = new FlowExecutorCPUWithChef<T>(ddOven, queueManager);
                ExternalCPUChef<T> externalCPUChef = new ExternalCPUChef<>(executorCPUWithChef);
                List<QueuedTaskModel<T>> queuedTaskList = new ArrayList<>();
                for (FlowNode<?> f : graph.getNodes()) {
                    QueuedTaskModel<T> queuedTask = buildQueuedTask(f, TaskType.CPU);
                    queuedTaskList.add(queuedTask);
                }
                int queueType = queueManager.put(queuedTaskList, 1);
                if (queueType == 1) {
                    allResults.putAll(executorCPUWithChef.execute(1, 1));
                } else {
                    allResults.putAll(externalCPUChef.execute(1, 0));
                }
            }
        } else {
            try (VirtualThreadOven oven = new VirtualThreadOven();) {
                FlowExecutorIOWithChef<T> executorIOWithChef = new FlowExecutorIOWithChef<T>(oven, queueManager);
                ExternalIOChef<T> externalIOChef = new ExternalIOChef<>(executorIOWithChef);
                List<QueuedTaskModel<T>> queuedTaskList = new ArrayList<>();
                for (FlowNode<?> f : graph.getNodes()) {
                    QueuedTaskModel<T> queuedTask = buildQueuedTask(f, TaskType.IO);
                    queuedTaskList.add(queuedTask);
                }
                int queueType = queueManager.put(queuedTaskList, 0);
                if (queueType == 1) {
                    allResults.putAll(executorIOWithChef.execute(0, 1));
                } else {
                    allResults.putAll(externalIOChef.execute(0, 0));
                }
            }
        }
        return allResults;
    }

    public QueuedTaskModel<T> buildQueuedTask(FlowNode<?> node, TaskType taskType) {
        long submitTime = System.currentTimeMillis();
        return new QueuedTaskModel<T>(node, taskType, node.getId(), submitTime, 1, 1);
    }
}
