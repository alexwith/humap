package com.github.alexwith.humap.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.proxy.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DirtyTrackingTest extends TestHumap {

    @BeforeAll
    public static void setup() {
        Humap.get().connect("mongodb://localhost:27017", "test");
    }

    @Test
    public void noChangesTest() {
        this.applyEntity(this.sampleEntity(), (entity) -> {
            assertFalse(this.isDirty(entity, "string"));
            assertFalse(this.isDirty(entity, "number"));
            assertFalse(this.isDirty(entity, "list"));
            assertFalse(this.isDirty(entity, "map"));
            assertFalse(this.isDirty(entity, "entityList"));
            assertFalse(this.isDirty(entity, "entityMap"));
        });
    }

    @Test
    public void normalFieldTest() {
        this.applyEntity(this.sampleEntity(), (entity) -> {
            entity.setString("New text");
            entity.setNumber(20);

            assertTrue(this.isDirty(entity, "string"));
            assertTrue(this.isDirty(entity, "number"));

            assertFalse(this.isDirty(entity, "list"));
            assertFalse(this.isDirty(entity, "map"));
            assertFalse(this.isDirty(entity, "entityList"));
            assertFalse(this.isDirty(entity, "entityMap"));
        });
    }

    // a collection in this context includes maps
    @Test
    public void collectionFieldTest() {
        this.applyEntity(this.sampleEntity(), (entity) -> {
            entity.getList().add("Line 3");
            entity.getMap().remove("Key 1");

            assertTrue(this.isDirty(entity, "list"));
            assertTrue(this.isDirty(entity, "map"));

            assertFalse(this.isDirty(entity, "string"));
            assertFalse(this.isDirty(entity, "number"));
            assertFalse(this.isDirty(entity, "entityList"));
            assertFalse(this.isDirty(entity, "entityMap"));
        });
    }

    @Test
    public void collectionEntityFieldTest() {
        this.applyEntity(this.sampleEntity(), (entity) -> {
            entity.getEntityList().get(0).setString("New text");
            entity.getEntityMap().get("Test").setString("New text");

            assertTrue(this.isDirty(entity, "entityList"));
            assertTrue(this.isDirty(entity, "entityMap"));

            assertFalse(this.isDirty(entity, "string"));
            assertFalse(this.isDirty(entity, "number"));
            assertFalse(this.isDirty(entity, "list"));
            assertFalse(this.isDirty(entity, "map"));
        });
    }

    @Test
    public void deepCollectionEntityFieldTest() {
        this.applyEntity(this.sampleEntity(), (entity) -> {
            entity.getSubEntity().getStringList().add("New Text");
            entity.getEntityList().get(0).getStringList().add("New Text");

            assertTrue(this.isDirty(entity, "subEntity"));
            assertTrue(this.isDirty(entity, "entityList"));

            assertFalse(this.isDirty(entity, "entityMap"));
            assertFalse(this.isDirty(entity, "string"));
            assertFalse(this.isDirty(entity, "number"));
            assertFalse(this.isDirty(entity, "list"));
            assertFalse(this.isDirty(entity, "map"));
        });
    }

    private boolean isDirty(Entity entity, String path) {
        final DirtyTracker tracker = Proxy.asProxy(entity).getDirtyTracker();
        return tracker.isDirty(path);
    }

    private TestEntity sampleEntity() {
        return new TestEntity(
            UUID.randomUUID(),
            "Text",
            11,
            this.sampleSubEntity(),
            new ArrayList<>(List.of("Line 1", "Line 2")),
            new HashMap<>(Map.of("Key 1", 1)),
            new ArrayList<>(List.of(this.sampleSubEntity())),
            new HashMap<>(Map.of("Test", this.sampleSubEntity()))
        ).proxy();
    }

    private TestSubEntity sampleSubEntity() {
        return new TestSubEntity("Text", new ArrayList<>()).proxy();
    }

    @Collection("test")
    public static class TestEntity implements IdEntity<UUID> {
        private @EntityId UUID id;
        private String string;
        private int number;
        private TestSubEntity subEntity;
        private List<String> list;
        private Map<String, Integer> map;
        private List<TestSubEntity> entityList;
        private Map<String, TestSubEntity> entityMap;

        public TestEntity(UUID id,
                          String string,
                          int number,
                          TestSubEntity subEntity,
                          List<String> list,
                          Map<String, Integer> map,
                          List<TestSubEntity> entityList,
                          Map<String, TestSubEntity> entityMap) {
            this.id = id;
            this.string = string;
            this.number = number;
            this.subEntity = subEntity;
            this.list = list;
            this.map = map;
            this.entityList = entityList;
            this.entityMap = entityMap;
        }

        protected TestEntity() {}

        @Override
        public UUID getId() {
            return this.id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getString() {
            return this.string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public int getNumber() {
            return this.number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public TestSubEntity getSubEntity() {
            return this.subEntity;
        }

        public List<String> getList() {
            return this.list;
        }

        public Map<String, Integer> getMap() {
            return this.map;
        }

        public List<TestSubEntity> getEntityList() {
            return this.entityList;
        }

        public Map<String, TestSubEntity> getEntityMap() {
            return this.entityMap;
        }
    }

    public static class TestSubEntity implements Entity {
        private String string;
        private List<String> stringList;

        public TestSubEntity(String string, List<String> stringList) {
            this.string = string;
            this.stringList = stringList;
        }

        protected TestSubEntity() {}

        public String getString() {
            return this.string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public List<String> getStringList() {
            return this.stringList;
        }
    }
}