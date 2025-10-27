package org.virtual.core;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class VirtualLahmacunTask<T> {
    private final Supplier<T> supplier;

    public VirtualLahmacunTask(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public CompletableFuture<T> run(VirtualThreadOven oven) {
        return oven.submit(supplier);
    }

    public CompletableFuture<T> ddRun(DedicatedPoolThreadOven ddOven) {
        return ddOven.submit(supplier);
    }
}
