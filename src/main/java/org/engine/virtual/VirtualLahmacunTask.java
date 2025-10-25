package org.engine.virtual;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class VirtualLahmacunTask<T> {
    private final CompletableFuture<T> future;

    public VirtualLahmacunTask(Supplier<T> supplier) {
        this.future = CompletableFuture.supplyAsync(supplier);
    }

    public T join()  {
        return future.join();
    }

    public CompletableFuture<T> future() {
        return future;
    }
}
