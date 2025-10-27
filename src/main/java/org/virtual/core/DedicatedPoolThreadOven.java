package org.virtual.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class DedicatedPoolThreadOven implements AutoCloseable {

    private final ExecutorService executor;

    public DedicatedPoolThreadOven() {
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor);
    }

    public void waitAll(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
    }

    @Override
    public void close() throws Exception {
    executor.shutdown();
    }
}
