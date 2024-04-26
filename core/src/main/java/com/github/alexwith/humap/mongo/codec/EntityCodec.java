package com.github.alexwith.humap.mongo.codec;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.util.PrimitiveTypes;
import com.github.alexwith.humap.util.SneakyThrows;
import java.lang.reflect.Constructor;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class EntityCodec implements Codec<Entity> {
    private final CodecRegistry registry;
    private final EntitySpec spec;

    public EntityCodec(CodecRegistry registry, EntitySpec spec) {
        this.registry = registry;
        this.spec = spec;
    }

    @Override
    public Entity decode(BsonReader reader, DecoderContext context) {
        final Entity entity = (Entity) SneakyThrows.supply(() -> {
            final Constructor<?> constructor = this.spec.getOriginClass().getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        });

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            final String name = reader.readName();
            if (name.equals("_id")) {
                final EntityField idField = this.spec.getIdField();
                final Codec<?> codec = this.registry.get(PrimitiveTypes.toBoxedType(idField.getType()));
                final Object value = codec.decode(reader, context);
                idField.setValue(entity, value);
                continue;
            }

            final EntityField field = this.spec.getField(name);
            if (field == null) {
                reader.skipValue();
                continue;
            }

            final Class<?> type = PrimitiveTypes.toBoxedType(field.getType());
            final Codec<?> codec = this.registry.get(type);
            final Object value = codec.decode(reader, context);
            field.setValue(entity, value);
        }

        reader.readEndDocument();

        if (entity instanceof IdEntity<?> rootEntity) {
            return Entity.proxy(rootEntity);
        }

        return entity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encode(BsonWriter writer, Entity entity, EncoderContext context) {
        final Proxy proxy = Proxy.asProxy(entity);
        final DirtyTracker dirtyTracker = proxy.getDirtyTracker();

        writer.writeStartDocument();

        for (final EntityField field : this.spec.getFields().values()) {
            if (field == this.spec.getIdField()) {
                continue;
            }

            final String name = field.getName();
            if (!dirtyTracker.isAllDirty() && !field.isProxyable() && !dirtyTracker.isDirty(name)) {
                continue;
            }

            writer.writeName(name);

            final Class<?> type = PrimitiveTypes.toBoxedType(field.getType());
            final Object value = field.getValue(entity);
            final Encoder<Object> encoder = (Encoder<Object>) this.registry.get(type);
            encoder.encode(writer, value, context);
        }

        dirtyTracker.setAllDirty(false);

        writer.writeEndDocument();
    }

    @Override
    public Class<Entity> getEncoderClass() {
        return Entity.class;
    }
}
