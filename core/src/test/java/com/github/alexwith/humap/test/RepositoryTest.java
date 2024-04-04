package com.github.alexwith.humap.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.repository.Repository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RepositoryTest {

    @BeforeAll
    public static void setup() {
        Humap.get(); // init humap
    }

    @Test
    public void getTest() {
        final EntityRepository repository = Repository.get(EntityRepository.class);
        assertNotNull(repository);
        assertInstanceOf(EntityRepository.class, repository);
    }

    @Test
    public void consumeTest() {
        Repository.consume(EntityRepository.class, (repository) -> {
            assertNotNull(repository);
            assertInstanceOf(EntityRepository.class, repository);
        });
    }

    @Test
    public void applyTest() {
        final String returned = Repository.apply(EntityRepository.class, (repository) -> {
            assertNotNull(repository);
            assertInstanceOf(EntityRepository.class, repository);

            return "test";
        });

        assertEquals("test", returned);
    }

    @Test
    public void entityClassTest() {
        Repository.consume(EntityRepository.class, (repository) -> {
            assertEquals(Entity.class, repository.getEntityClass());
        });
    }

    public interface EntityRepository extends Repository<UUID, Entity> {}

    @Collection("entity")
    public static class Entity implements IdEntity<UUID> {
        private @EntityId UUID id;
        private String name;

        public Entity(UUID id, String name) {
            this.id = id;
            this.name = name;
        }

        protected Entity() {}

        @Override
        public UUID getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
