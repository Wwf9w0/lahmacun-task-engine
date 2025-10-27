package org.virtual.core;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class VirtualThreadOven implements AutoCloseable {

    private final ExecutorService executor;
    private static final int MAX_THREAD = 100;

    public VirtualThreadOven() {
        ThreadFactory factory = new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                if (count.incrementAndGet() > MAX_THREAD) {
                    throw new IllegalStateException("Max virtual thread limit reached: " + MAX_THREAD);
                }
                Thread t = Thread.ofVirtual().name("vthread-", count.get()).unstarted(r);
                t.setDaemon(true);
                return t;
            }
        };
        this.executor = Executors.newThreadPerTaskExecutor(factory);
    }

    public <T> CompletableFuture<T> submit(Supplier<T> task) {
        return CompletableFuture.supplyAsync(task, executor);
    }

    public void waitAll(CompletableFuture<?>... futures) {
        CompletableFuture.allOf(futures).join();
    }


    @Override
    public void close() {
        executor.shutdown();
    }
}
