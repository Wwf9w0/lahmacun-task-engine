package org.virtual.annotation;

import org.virtual.core.FlowExecutorWithChef;
import org.virtual.core.VirtualLahmacunChef;
import org.virtual.dag.FlowGraph;
import org.virtual.virtual.model.TaskLahmacunResult;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FlowMethodExecutor {
    private final VirtualLahmacunChef chef;

    public FlowMethodExecutor(VirtualLahmacunChef chef) {
        this.chef = chef;
    }

    public Map<String, TaskLahmacunResult<?>> execute(Object... services) throws Exception {
        Map<String, TaskLahmacunResult<?>> allResults = new LinkedHashMap<>();
        List<CompletableFuture<Map<String, TaskLahmacunResult<?>>>> futures = new ArrayList<>();

        for (Object service : services) {
            for (Method method : service.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(VirtualFlow.class)) {
                    method.setAccessible(true);

                    CompletableFuture<Map<String, TaskLahmacunResult<?>>> future = CompletableFuture.supplyAsync(() -> {
                        try {
                            Object result = method.invoke(service);
                            if (!(result instanceof FlowGraph graph)) {
                                throw new IllegalStateException("Method " + method.getName() + " must return FlowGraph");
                            }
                            FlowExecutorWithChef executor = new FlowExecutorWithChef(graph, chef);
                            return executor.execute();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, chef.oven().getExecutor());

                    futures.add(future);
                }
            }
        }

        chef.waitAll(futures.toArray(new CompletableFuture[0]));

        for (CompletableFuture<Map<String, TaskLahmacunResult<?>>> f : futures) {
            allResults.putAll(f.join());
        }

        return allResults;
    }
}
