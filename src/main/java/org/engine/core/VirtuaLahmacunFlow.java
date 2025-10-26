package org.engine.core;

import org.engine.virtual.model.TaskLahmacunResult;
import org.engine.virtual.model.VirtualLahmacunTask;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class VirtuaLahmacunFlow {
    private final VirtuaLahmacunlChef chef;
    private final List<VirtualLahmacunTask<?>> tasks = new ArrayList<>();

    public VirtuaLahmacunFlow(VirtuaLahmacunlChef chef) {
        this.chef = chef;
    }

    public <T> VirtualLahmacunTask<T> addTask(Supplier<T> supplier) {
        VirtualLahmacunTask<T> task = new VirtualLahmacunTask<>(supplier);
        tasks.add(task);
        return task;
    }

    public List<TaskLahmacunResult<?>> bakeAll() {
        return chef.run(tasks);
    }
}
