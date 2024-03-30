package com.github.alexwith.humap.repository;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.ProxyFactory;
import com.github.alexwith.humap.repository.query.Query;
import com.github.alexwith.humap.repository.query.QueryImpl;
import com.github.alexwith.humap.repository.query.QueryInterceptorImpl;
import com.github.alexwith.humap.util.SneakyThrows;
import com.github.alexwith.humap.util.TypeResolver;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;

public class RepositoryManagerImpl implements RepositoryManager {
    private final Map<Class<?>, Repository<?, ?>> repositories = new ConcurrentHashMap<>();

    private static final ClassLoader CLASS_LOADER = RepositoryManager.class.getClassLoader();

    @Override
    @SuppressWarnings("unchecked")
    public <K, T extends IdEntity<K>, U extends Repository<K, T>> U get(Class<U> repositoryClass) {
        return (U) this.repositories.computeIfAbsent(repositoryClass, ($) -> this.createRepository(repositoryClass));
    }

    @SuppressWarnings("unchecked")
    private <K, T extends IdEntity<K>, U extends Repository<K, T>> U createRepository(Class<U> clazz) {
        final Class<T> entityClass = (Class<T>) TypeResolver.resolveRawArguments(Repository.class, clazz)[1];
        if (EntitySpec.from(entityClass) == null) {
            final ProxyFactory proxyFactory = Instances.get(ProxyFactory.class);
            proxyFactory.getProxyCreator(entityClass); // inits the entity spec
        }

        DynamicType.Builder<U> builder = new ByteBuddy().subclass(clazz);

        final Set<QueryInterceptorImpl<K, T>> queryInterceptors = new HashSet<>();
        for (final Map.Entry<Method, Query> entry : this.parseQueries(clazz).entrySet()) {
            final Method method = entry.getKey();
            final Query query = entry.getValue();

            final QueryInterceptorImpl<K, T> queryInterceptor = new QueryInterceptorImpl<>(query);
            queryInterceptors.add(queryInterceptor);

            builder = builder
                .defineMethod(method.getName(), method.getReturnType(), Visibility.PUBLIC)
                .withParameters(method.getParameterTypes())
                .intercept(MethodDelegation.to(queryInterceptor));
        }

        final Class<? extends U> proxiedClass = builder.make().load(CLASS_LOADER).getLoaded();
        final U repository = SneakyThrows.supply(() -> proxiedClass.getDeclaredConstructor().newInstance());

        for (final QueryInterceptorImpl<K, T> queryInterceptor : queryInterceptors) {
            queryInterceptor.assignRepository(repository);
        }

        return repository;
    }

    private Map<Method, Query> parseQueries(Class<?> clazz) {
        final Map<Method, Query> queries = new HashMap<>();
        for (final Method method : clazz.getDeclaredMethods()) {
            QueryImpl.parse(method).ifPresent((query) -> {
                queries.put(method, query);
            });
        }

        return queries;
    }
}
