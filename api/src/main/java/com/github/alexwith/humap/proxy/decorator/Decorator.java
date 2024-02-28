package com.github.alexwith.humap.proxy.decorator;

import net.bytebuddy.dynamic.DynamicType;

public interface Decorator {

    /**
     * Modifies the builder by applying the
     * decorator and returns the modified
     * builder
     *
     * @param builder The builder to append to
     * @return The modified builder
     */
    DynamicType.Builder<?> apply(DynamicType.Builder<?> builder);
}
