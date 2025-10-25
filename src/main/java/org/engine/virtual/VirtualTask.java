package org.engine.virtual;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class VirtualTask<T> {
    private final CompletableFuture<T> future;

    public VirtualTask(Supplier<T> supplier) {
        this.future = CompletableFuture.supplyAsync(supplier);
    }

    public T join()  {
        return future.join();
    }

    public CompletableFuture<T> future() {
        return future;
    }
}
