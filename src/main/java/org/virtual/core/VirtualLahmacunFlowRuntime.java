package org.virtual.core;

import org.virtual.annotation.FlowType;
import org.virtual.annotation.VirtualFlow;
import org.virtual.dag.FlowGraph;
import org.virtual.dag.FlowNode;
import org.virtual.model.TaskLahmacunResult;
import org.virtual.model.TaskType;
import org.virtual.queue.QueueManager;
import org.virtual.model.QueuedTaskModel;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class VirtualLahmacunFlowRuntime<T> {
    private final QueueManager queueManager;

    public VirtualLahmacunFlowRuntime(QueueManager<T> queueManager) {
        this.queueManager = queueManager;
    }

    public void startAllFlows(String basePackage) throws Exception {
        Assert.nonNull(basePackage, "Base package must not be null");
        List<Class<?>> classes = scanPackage(basePackage);
        Map<String, TaskLahmacunResult<?>> allResults = new LinkedHashMap<>();
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotation() || clazz.isInterface()) {
                continue;
            }
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(VirtualFlow.class)) {
                    method.setAccessible(true);
                    Object result = method.invoke(instance);
                    if (!(result instanceof FlowGraph graph)) {
                        throw new IllegalStateException("@" + VirtualFlow.class.getSimpleName()
                                + " method must return FlowGraph: " + method.getName());
                    }
                    dynamicRun(method, allResults, graph);
                }
            }
        }
        System.out.println("\n--- VirtualFlow Runtime Results ---");
        allResults.forEach((id, r) -> System.out.println(id + " -> " + r.status() + " | " + r.result() + " | " + r.durationMillis()));
        AtomicLong sum = new AtomicLong();
        allResults.forEach((k, v) -> sum.addAndGet(v.durationMillis()));
        System.out.println("Completed Flows -> " + sum + " ms");
    }


    private List<Class<?>> scanPackage(String basePackage) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = basePackage.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File dir = new File(resource.getFile());
            if (dir.exists()) {
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    if (file.getName().endsWith(".class")) {
                        String className = basePackage + '.' + file.getName().replace(".class", "");
                        classes.add(Class.forName(className));
                    }
                }
            }
        }
        return classes;
    }

    private Map<String, TaskLahmacunResult<?>> dynamicRun(Method method, Map<String, TaskLahmacunResult<?>> allResults, FlowGraph graph) throws Exception {
        if (method.isAnnotationPresent(FlowType.class)) {
            FlowType flowType = method.getAnnotation(FlowType.class);
            TaskType taskType = flowType.value();
            if (taskType == TaskType.IO) {
                try (VirtualThreadOven oven = new VirtualThreadOven();) {
                    FlowExecutorIOWithChef<T> executorIOWithChef = new FlowExecutorIOWithChef<T>(graph, oven, queueManager);
                    List<QueuedTaskModel<T>> queuedTaskList = new ArrayList<>();
                    for (FlowNode<?> f : graph.getNodes()) {
                        QueuedTaskModel<T> queuedTask = buildQueuedTask(f, TaskType.IO);
                        queuedTaskList.add(queuedTask);
                    }
                    queueManager.put(queuedTaskList, 0);
                    allResults.putAll(executorIOWithChef.execute(0));
                }
            } else {
                try (DedicatedPoolThreadOven ddOven = new DedicatedPoolThreadOven();) {
                    FlowExecutorCPUWithChef executorCPUWithChef = new FlowExecutorCPUWithChef(graph, ddOven);
                    allResults.putAll(executorCPUWithChef.execute());
                }
            }
        }
        return allResults;
    }

    public QueuedTaskModel<T> buildQueuedTask(FlowNode<?> node, TaskType taskType) {
        long submitTime = System.currentTimeMillis();
        return new QueuedTaskModel<T>(node, taskType, node.getId(), submitTime, 1);
    }
}