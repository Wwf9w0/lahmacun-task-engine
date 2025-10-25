package org.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class LahmacunVirtualFlow {
    private final LahmacunChef chef;
    private final List<Supplier<?>> tasks = new ArrayList<>();

    public LahmacunVirtualFlow(LahmacunChef chef) {
        this.chef = chef;
    }

    public <T> LahmacunVirtualFlow addTask(Supplier<T> task) {
        tasks.add(task);
        return this;
    }

    public List<TaskResult<?>> bakeAll() {
        return chef.run(this.tasks);
    }
}
