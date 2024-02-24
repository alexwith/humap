package com.github.alexwith.humap.proxy;

public interface Morpher {

    <T> T morph(Object... args);
}