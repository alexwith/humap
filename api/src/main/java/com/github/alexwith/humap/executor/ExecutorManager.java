package com.github.alexwith.humap.executor;

import java.util.concurrent.Executor;

public interface ExecutorManager {

    default Executor getExecutorOrDefault(String name) {
        if (name == null) {
            return this.getDefaultExecutor();
        }

        final Executor executor = this.getExecutor(name);
        return executor == null ? this.getDefaultExecutor() : executor;
    }

    Executor getDefaultExecutor();

    Executor getExecutor(String name);

    void registerExecutor(String name, Executor executor);
}
