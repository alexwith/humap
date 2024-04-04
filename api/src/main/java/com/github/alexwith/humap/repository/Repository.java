package com.github.alexwith.humap.repository;

import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.mongo.MongoEntityManager;
import com.mongodb.client.model.Filters;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.bson.conversions.Bson;

/**
 * The {@link Repository} is responsible for interfacing
 * between the database, and it's respective entity type
 */
public interface Repository<K, T extends IdEntity<K>> {

    /**
     * Gets an instance of the specified repository
     *
     * @param repositoryClass The wanted repository
     * @return An instance of the specified repository class
     */
    static <K, T extends IdEntity<K>, U extends Repository<K, T>> U get(Class<U> repositoryClass) {
        return Instances.get(RepositoryManager.class).get(repositoryClass);
    }

    /**
     * Supplies an instance of the specified repository
     *
     * @param repositoryClass The wanted repository
     * @param consumer        The consumer the repository should be supplied to
     */
    static <K, T extends IdEntity<K>, U extends Repository<K, T>> void consume(Class<U> repositoryClass, Consumer<U> consumer) {
        consumer.accept(Repository.get(repositoryClass));
    }

    /**
     * Supplies an instance of the specified repository,
     * and returns the result of the function
     *
     * @param repositoryClass The wanted repository
     * @param function        The function where the repository will be supplied to and produce a result
     * @return The result produced by the function
     */
    static <S, K, T extends IdEntity<K>, U extends Repository<K, T>> S apply(Class<U> repositoryClass, Function<U, S> function) {
        return function.apply(Repository.get(repositoryClass));
    }

    /**
     * Returns the entity class associated with this repository
     * <br>
     * This method is implemented through
     * bytebuddy at runtime
     *
     * @return The entity class
     */
    default Class<T> getEntityClass() {
        return null;
    }

    default T findById(K id) {
        return this.findOne(Filters.eq("_id", id));
    }

    default T findOne(Bson query) {
        return MongoEntityManager.get(this.getEntityClass()).findOne(query);
    }

    default List<T> findAll(Bson query) {
        return MongoEntityManager.get(this.getEntityClass()).findAll(query);
    }
}
