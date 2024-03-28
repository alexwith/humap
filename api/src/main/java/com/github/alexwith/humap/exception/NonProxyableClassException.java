package com.github.alexwith.humap.exception;

public class NonProxyableClassException extends RuntimeException {

    public NonProxyableClassException(Class<?> clazz) {
        super("The class %s cannot be proxied.".formatted(clazz));
    }
}
