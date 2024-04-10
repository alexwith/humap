package com.github.alexwith.humap.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class ExecutorManagerImpl implements ExecutorManager {
    private final Map<String, Executor> executors = new HashMap<>();

    private static final Executor DEFAULT_EXECUTOR = ForkJoinPool.commonPool();

    @Override
    public Executor getDefaultExecutor() {
        return DEFAULT_EXECUTOR;
    }

    @Override
    public Executor getExecutor(String name) {
        return this.executors.get(name);
    }

    @Override
    public void registerExecutor(String name, Executor executor) {
        this.executors.put(name, executor);
    }
}
