package com.github.alexwith.humap.proxy.morphing;

import java.util.function.Consumer;

public class CallbackMorpher implements Morpher {
    private final Morpher morpher;
    private final Consumer<Object[]> onNewArgs;

    public CallbackMorpher(Morpher morpher, Consumer<Object[]> onNewArgs) {
        this.morpher = morpher;
        this.onNewArgs = onNewArgs;
    }

    @Override
    public <T> T morph(Object... args) {
        this.onNewArgs.accept(args);
        return this.morpher.morph(args);
    }
}
