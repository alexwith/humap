package com.github.alexwith.humap.mongo.codec;

import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.spec.EntitySpec;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class EntityCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (Entity.class.isAssignableFrom(clazz)) {
            final EntitySpec spec = EntitySpec.from((Class<? extends Entity>) clazz);
            return (Codec<T>) new EntityCodec(registry, spec);
        }

        return null;
    }
}