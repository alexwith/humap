package com.github.alexwith.humap.exception;

public class NoCollectionSpecifiedException extends RuntimeException {

    public NoCollectionSpecifiedException(Class<?> clazz) {
        super("The class %s needs to specify a collection.".formatted(clazz));
    }
}
