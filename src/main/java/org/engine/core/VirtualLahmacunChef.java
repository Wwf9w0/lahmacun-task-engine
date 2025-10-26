package org.engine.core;

import org.engine.virtual.model.LahmacunTaskStatus;
import org.engine.virtual.model.TaskLahmacunResult;
import org.engine.virtual.model.VirtualLahmacunTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class VirtualLahmacunChef {
    private final VirtualThreadOven oven;

    public VirtualLahmacunChef(VirtualThreadOven oven) {
        this.oven = oven;
    }

    public List<TaskLahmacunResult<?>> run(List<VirtualLahmacunTask<?>> tasks) {
        List<Future<TaskLahmacunResult<?>>> futures = new ArrayList<>();
        for (VirtualLahmacunTask<?> task : tasks) {
            futures.add(oven.submit(() -> {
                long start = System.currentTimeMillis();
                try {
                    Object res = task.future().get();
                    long duration = System.currentTimeMillis() - start;
                    return new TaskLahmacunResult<>(task.getClass().getSimpleName(), LahmacunTaskStatus.SUCCESS, res, null, duration);
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - start;
                    return new TaskLahmacunResult<>(task.getClass().getSimpleName(), LahmacunTaskStatus.FAILED, e, null, duration);
                }
            }));
        }

        List<TaskLahmacunResult<?>> results = new ArrayList<>();
        for (Future<TaskLahmacunResult<?>> f : futures) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                results.add(new TaskLahmacunResult<>("unknown-task", LahmacunTaskStatus.FAILED, e, null, 0));
            }
        }
        return results;
    }

 /*   public void waitAll(CompletableFuture<?>... futures) {
        oven.waitAll(futures);
    } */
}
