package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.exception.NoCollectionSpecifiedException;
import com.github.alexwith.humap.mongo.codec.EntityCodecProvider;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public class MongoEntityManagerImpl<K, T extends IdEntity<K>> implements MongoEntityManager<K, T> {
    private final Class<T> entityClass;
    private final EntitySpec spec;
    private final MongoCollection<T> collection;
    private final Cache<T, Lock> savingLocks = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.SECONDS)
        .build();

    private static final CodecRegistry CODEC_REGISTRY = CodecRegistries.fromRegistries(
        CodecRegistries.fromProviders(new EntityCodecProvider()),
        CodecRegistries.fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)),
        MongoClientSettings.getDefaultCodecRegistry()
    );
    private static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);

    public MongoEntityManagerImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.spec = EntitySpec.from(entityClass);
        this.collection = this.initCollection();
    }

    @Override
    public T findOne(Bson query) {
        final T entity = this.collection.find(query).first();

        return entity;
    }

    @Override
    public void save(T entity) {
        final Lock savingLock = this.savingLocks.get(entity, ($) -> new ReentrantLock());
        savingLock.lock();
        try {
            final BsonDocument document = BsonDocumentWrapper.asBsonDocument(entity, CODEC_REGISTRY);

            final List<Bson> updates = new ArrayList<>();
            this.documentToUpdates(updates, "", document);

            if (updates.isEmpty()) {
                return;
            }

            final Bson idQuery = Filters.eq("_id", entity.getId());
            this.collection.updateOne(idQuery, Updates.combine(updates), UPDATE_OPTIONS);

            final DirtyTracker dirtyTracker = Proxy.asProxy(entity).getDirtyTracker();
            dirtyTracker.setAllDirty(false);
        } finally {
            savingLock.unlock();
        }
    }

    private void documentToUpdates(List<Bson> updates, String prefix, BsonDocument document) {
        document.forEach((field, value) -> {
            if (value instanceof final BsonDocument innerDocument) {
                this.documentToUpdates(updates, field + ".", innerDocument);
                return;
            }

            updates.add(Updates.set(prefix + field, value));
        });
    }

    private MongoCollection<T> initCollection() {
        final MongoDatabase database = Humap.get().getConnection().getDatabase();
        final String name = this.spec.getCollection().orElseThrow(() -> new NoCollectionSpecifiedException(this.spec.getOriginClass()));
        return database.getCollection(name, this.entityClass).withCodecRegistry(CODEC_REGISTRY);
    }
}
