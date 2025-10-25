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

    public Future<Object> submit(Callable<Object> callable) {
        return oven.executor().submit(callable);
    }

    public List<Object> run(List<LahmacunTask<?>> tasks) {
        List<Future<Object>> futures = new ArrayList<>();
        for (LahmacunTask<?> task : tasks) {
            futures.add(this.submit(() -> task.call()));
        }

        List<Object> results = new ArrayList<>();
        for (Future<Object> f : futures) {
            try {
                results.add(f.get());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return results;
    }
}
