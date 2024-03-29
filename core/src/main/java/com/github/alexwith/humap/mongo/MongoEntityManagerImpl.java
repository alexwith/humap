package com.github.alexwith.humap.mongo;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.exception.NoCollectionSpecifiedException;
import com.github.alexwith.humap.mongo.codec.EntityCodecProvider;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import java.util.Map;
import org.bson.BsonDocument;
import org.bson.BsonDocumentWrapper;
import org.bson.BsonValue;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

public class MongoEntityManagerImpl implements MongoEntityManager {
    private final EntitySpec spec;
    private final MongoCollection<Entity> collection;

    private static final CodecRegistry CODEC_REGISTRY = CodecRegistries.fromRegistries(
        CodecRegistries.fromProviders(new EntityCodecProvider()),
        CodecRegistries.fromCodecs(new UuidCodec(UuidRepresentation.STANDARD)),
        MongoClientSettings.getDefaultCodecRegistry()
    );
    private static final UpdateOptions UPDATE_OPTIONS = new UpdateOptions().upsert(true);

    public MongoEntityManagerImpl(EntitySpec spec) {
        this.spec = spec;
        this.collection = this.initCollection();
    }

    @Override
    public void save(IdEntity<?> entity) {
        final BsonDocument document = BsonDocumentWrapper.asBsonDocument(entity, CODEC_REGISTRY);

        int updateIndex = 0;
        final Bson[] updates = new Bson[document.size()];
        for (final Map.Entry<String, BsonValue> entry : document.entrySet()) {
            updates[updateIndex++] = Updates.set(entry.getKey(), entry.getValue());
        }

        final Bson idQuery = this.createIdQuery(entity);
        this.collection.updateOne(idQuery, Updates.combine(updates), UPDATE_OPTIONS);
    }

    private Bson createIdQuery(IdEntity<?> entity) {
        return Filters.eq("_id", entity.getId());
    }

    private MongoCollection<Entity> initCollection() {
        final MongoDatabase database = Humap.get().getConnection().getDatabase();
        final String name = this.spec.getCollection().orElseThrow(() -> new NoCollectionSpecifiedException(this.spec.getOriginClass()));
        return database.getCollection(name, Entity.class).withCodecRegistry(CODEC_REGISTRY);
    }
}
