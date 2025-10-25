package org.engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class VirtualThreadOven implements AutoCloseable {
    private final ExecutorService executor;

    public VirtualThreadOven() {
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    public ExecutorService executor() {
        return executor;
    }

    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }
}
