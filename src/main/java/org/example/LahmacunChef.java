package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public final class LahmacunChef {
    private final VirtualThreadOven oven;

    public LahmacunChef(VirtualThreadOven oven) {
        this.oven = oven;
    }

    public Future<TaskResult<?>> submit(LahmacunTask<?> task) {
        return oven.executor().submit(() -> {
            long start = System.currentTimeMillis();
            try {
                Object result = task.call();
                long duration = System.currentTimeMillis() - start;
                return new TaskResult<>(task.name(), TaskStatus.SUCCESS, result, null, duration);
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - start;
                return new TaskResult<>(task.name(), TaskStatus.FAILED, e, null, duration);
            }
        });
    }

    public List<TaskResult<?>> run(List<LahmacunTask<?>> tasks) {
        List<Future<TaskResult<?>>> futures = new ArrayList<>();
        for (LahmacunTask<?> task : tasks) {
            futures.add(this.submit(task));
        }
        List<TaskResult<?>> results = new ArrayList<>();
        for (Future<TaskResult<?>> f : futures) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                results.add(new TaskResult<>("unknown-task", TaskStatus.FAILED, null, e, 0));
            }
        }
        return results;
    }
}
