package org.engine.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public final class VirtualThreadOven implements AutoCloseable {
    private final ExecutorService executor;

    public VirtualThreadOven() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public ExecutorService executor() {
        return executor;
    }

    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor);
    }

    public CompletableFuture<Void> run(Runnable task) {
        return CompletableFuture.runAsync(task, executor);
    }

    public void waitAll(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
    }

    @Override
    public void close() throws Exception {
        executor.shutdown();
    }
}
