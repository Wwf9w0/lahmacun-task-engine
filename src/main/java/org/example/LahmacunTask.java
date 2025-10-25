package org.example;

import java.util.concurrent.Callable;

@FunctionalInterface
public interface LahmacunTask<T> extends Callable<T> {

    T call();

    default String name() {
        return this.getClass().getSimpleName();
    }
}
