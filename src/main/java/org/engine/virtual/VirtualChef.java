package org.engine.virtual;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class VirtualChef {
    private final VirtualThreadOven oven;

    public VirtualChef(VirtualThreadOven oven) {
        this.oven = oven;
    }

    public List<TaskResult<?>> run(List<VirtualTask<?>> tasks) {
        List<Future<TaskResult<?>>> futures = new ArrayList<>();
        for (VirtualTask<?> task : tasks) {
            futures.add(oven.submit(() -> {
                long start = System.currentTimeMillis();
                try {
                    Object res = task.future().get();
                    long duration = System.currentTimeMillis() - start;
                    return new TaskResult<>(task.getClass().getSimpleName(), TaskStatus.SUCCESS, res, null, duration);
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - start;
                    return new TaskResult<>(task.getClass().getSimpleName(), TaskStatus.FAILED, e, null, duration);
                }

            }));
        }

        List<TaskResult<?>> results = new ArrayList<>();
        for (Future<TaskResult<?>> f : futures) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                results.add(new TaskResult<>("unknown-task", TaskStatus.FAILED, e, null, 0));
            } finally {
                oven.executor().shutdown();
            }
        }
        return results;
    }
}
