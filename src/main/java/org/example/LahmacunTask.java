package org.example;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface LahmacunTask<R> extends Callable<R> {

    R call();

    default String name() {
        return this.getClass().getSimpleName();
    }
}
