package org.virtual.core;

public class Assert {

    public static void nonNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
