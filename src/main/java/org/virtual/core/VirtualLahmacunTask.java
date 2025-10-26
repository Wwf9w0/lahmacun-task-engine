package org.virtual.core;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class VirtualLahmacunTask<T> {
    private final CompletableFuture<T> future;

    public VirtualLahmacunTask(Supplier<T> supplier) {
        this.future = CompletableFuture.supplyAsync(supplier);
    }

    public CompletableFuture<T> future() {
        return future;
    }
}
