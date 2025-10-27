package org.virtual.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class DedicatedPoolThreadOven implements AutoCloseable {

    private final ExecutorService executor;

    public DedicatedPoolThreadOven() {
        int maxThreads = Math.min(Runtime.getRuntime().availableProcessors(), 100);
        this.executor = Executors.newFixedThreadPool(maxThreads);
    }

    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor).orTimeout(5, TimeUnit.SECONDS);
    }

    public void waitAll(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
    }
}
