package com.github.alexwith.humap.util;

/**
 * A util to avoid writing try-catch-es all over the place,
 * and rather replace it with a more functional approach
 */
public class SneakyThrows {

    public static void run(ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static <T> T supply(ThrowableSupplier<T> supplier) {
        try {
            return supplier.supply();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    @FunctionalInterface
    public interface ThrowableRunnable {

        void run() throws Throwable;
    }

    @FunctionalInterface
    public interface ThrowableSupplier<T> {

        T supply() throws Throwable;
    }
}
