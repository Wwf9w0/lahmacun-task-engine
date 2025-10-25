package org.engine.virtual;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class VirtuaLahmacunlChef {
    private final VirtualThreadOven oven;

    public VirtuaLahmacunlChef(VirtualThreadOven oven) {
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
                    return new TaskLahmacunResult<>(task.getClass().getSimpleName(), TaskLahmacunStatus.SUCCESS, res, null, duration);
                } catch (Exception e) {
                    long duration = System.currentTimeMillis() - start;
                    return new TaskLahmacunResult<>(task.getClass().getSimpleName(), TaskLahmacunStatus.FAILED, e, null, duration);
                }

            }));
        }

        List<TaskLahmacunResult<?>> results = new ArrayList<>();
        for (Future<TaskLahmacunResult<?>> f : futures) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                results.add(new TaskLahmacunResult<>("unknown-task", TaskLahmacunStatus.FAILED, e, null, 0));
            } finally {
                oven.executor().shutdown();
            }
        }
        return results;
    }

    public void waitAll(CompletableFuture<?>... futures) {
        oven.waitAll(futures);
    }
}
