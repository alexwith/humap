package com.github.alexwith.humap.mongo.codec;

import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntityField;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import com.github.alexwith.humap.proxy.Proxy;
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
        final EntitySpec spec = EntitySpec.from(entity);
        final Proxy proxy = Proxy.asProxy(entity);
        final DirtyTracker dirtyTracker = proxy.getDirtyTracker();

        writer.writeStartDocument();

        for (final EntityField field : spec.getFields().values()) {
            if (field == spec.getIdField()) {
                continue;
            }

            final String name = field.getName();
            if (!dirtyTracker.isAllDirty() && !field.isProxyable() && !dirtyTracker.isDirty(proxy.appendAbsolutePath(name))) {
                continue;
            }

            writer.writeName(name);

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
