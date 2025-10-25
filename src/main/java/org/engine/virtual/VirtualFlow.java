package org.engine.virtual;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class VirtualFlow {
    private final VirtualChef chef;
    private final List<VirtualTask<?>> tasks = new ArrayList<>();

    public VirtualFlow(VirtualChef chef) {
        this.chef = chef;
    }

    public <T> VirtualTask<T> addTask(Supplier<T> supplier) {
        VirtualTask<T> task = new VirtualTask<>(supplier);
        tasks.add(task);
        return task;
    }

    public List<TaskResult<?>> bakeAll() {
        return chef.run(tasks);
    }
}
