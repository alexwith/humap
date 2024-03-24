package com.github.alexwith.humap.instance;

import com.github.alexwith.humap.exception.NoInstanceException;
import com.github.alexwith.humap.repository.Repository;
import jakarta.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This is used when the api needs access to an instance created
 * in the core. Dependency injection should be the main method to handle
 * inversion of control, but this is used for practicality in situations
 * such as {@link Repository#get(Class)}
 */
public class Instances {
    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    /**
     * Get an instance from the instances registry
     * If the instance isn't present, a {@link }
     *
     * @param clazz The clazz you want an instance of
     * @return The instance
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public static <T> T get(@Nonnull Class<T> clazz) {
        final T instance = (T) INSTANCES.get(clazz);
        if (instance == null) {
            throw new NoInstanceException();
        }

        return instance;
    }

    /**
     * Registers instances into the instances registry
     *
     * @param instances The instances to register
     */
    public static void register(@Nonnull Object... instances) {
        for (final Object instance : instances) {
            INSTANCES.put(instance.getClass(), instance);

            // These are the interfaces that this class implements
            // This way we can get instances using the api classes
            for (final Class<?> contracts : instance.getClass().getInterfaces()) {
                INSTANCES.put(contracts, instance);
            }
        }
    }
}
