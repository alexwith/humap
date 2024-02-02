package com.github.alexwith.humap;

public class Humap {

    private static final Humap INSTANCE = new Humap();

    public static Humap get() {
        return INSTANCE;
    }
}