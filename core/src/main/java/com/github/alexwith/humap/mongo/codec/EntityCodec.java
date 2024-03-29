package com.github.alexwith.humap.mongo.codec;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class EntityCodec implements Codec<Entity> {
    private final CodecRegistry registry;

    public EntityCodec(CodecRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Entity decode(BsonReader reader, DecoderContext context) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void encode(BsonWriter writer, Entity entity, EncoderContext context) {
        final EntitySpec spec = entity.getSpec();

        writer.writeStartDocument();

        for (final EntityField field : spec.getFields().values()) {
            if (field == spec.getIdField()) {
                continue;
            }

            writer.writeName(field.getName());

            final Object value = field.getValue(entity);
            final Encoder<Object> encoder = (Encoder<Object>) this.registry.get(value.getClass());
            encoder.encode(writer, value, context);
        }

        writer.writeEndDocument();
    }

    @Override
    public Class<Entity> getEncoderClass() {
        return Entity.class;
    }
}
