package org.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Supplier;

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
            } finally {
                oven.shutdown();
            }
        });
    }

    public List<TaskResult<?>> run(List<Supplier<?>> tasks) {
        List<Future<TaskResult<?>>> futures = new ArrayList<>();
        for (Supplier<?> task : tasks) {
            futures.add(this.submit(() -> {
                long start = System.currentTimeMillis();
                try {
                    Object res = task.get();
                    long duration = System.currentTimeMillis() - start;
                    return new TaskResult<>(task.getClass().getSimpleName(), TaskStatus.SUCCESS, res, null, duration);
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - start;
                    return new TaskResult<>(task.getClass().getSimpleName(), TaskStatus.FAILED, null, e, duration);
                }
            }));
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
