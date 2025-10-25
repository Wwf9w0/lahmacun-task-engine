package org.engine;

import java.util.ArrayList;
import java.util.List;

public class LahmacunFlow {
    private final LahmacunChef chef;
    private final List<LahmacunTask<?>> tasks = new ArrayList<>();

    public LahmacunFlow(LahmacunChef chef) {
        this.chef = chef;
    }

    public <T> LahmacunFlow addTask(LahmacunTask<T> task) {
        tasks.add(task);
        return this;
    }

    public List<TaskResult<?>> bakeAll() {
        return chef.run(this.tasks);
    }
}
