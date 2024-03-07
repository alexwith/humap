package com.github.alexwith.humap.util;

/*
This is pretty much AtomicReference, but just a shorter name
and more simple as a workaround to the lambda effectively final
restriction
 */
public class Ref<T> {
    private T value;

    public Ref(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
