package org.virtual.core;

public class Assert {

    public static void nonNull(Object object, String message) {
        if (object == null)
            throw new IllegalArgumentException(message);
    }
}
