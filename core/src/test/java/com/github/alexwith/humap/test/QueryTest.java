package com.github.alexwith.humap.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.annotation.QuerySignature;
import com.github.alexwith.humap.repository.query.Query;
import com.github.alexwith.humap.repository.query.QueryImpl;
import com.github.alexwith.humap.util.SneakyThrows;
import com.mongodb.client.model.Filters;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class QueryTest {

    @BeforeAll
    public static void setup() {
        Humap.get(); // init humap
    }

    @Test
    public void test() {
        assertEquals(
            Filters.eq("name", "Alex"),
            this.resolveMethod("findByName", "Alex")
        );

        assertEquals(
            Filters.and(
                Filters.eq("name", "Alex"),
                Filters.eq("age", 21)
            ),
            this.resolveMethod("findByNameAndAge", "Alex", 21)
        );

        assertEquals(
            Filters.or(
                Filters.and(
                    Filters.eq("name", "Alex"),
                    Filters.eq("age", 19)
                ),
                Filters.eq("username", "alexwith")
            ),
            this.resolveMethod("findByNameAndAgeOrUsername", "Alex", 19, "alexwith")
        );

        assertEquals(
            Filters.lt("age", 30),
            this.resolveMethod("findByLtAge", 30)
        );

        assertEquals(
            Filters.and(
                Filters.eq("name", "Alex"),
                Filters.lte("age", 30)
            ),
            this.resolveMethod("findByNameAndLteAge", "Alex", 30)
        );

        assertEquals(
            Filters.gt("age", 30),
            this.resolveMethod("findByGtAge", 30)
        );

        assertEquals(
            Filters.and(
                Filters.eq("name", "Alex"),
                Filters.gte("age", 30)
            ),
            this.resolveMethod("findByNameAndGteAge", "Alex", 30)
        );

        assertEquals(
            Filters.or(
                Filters.eq("name", "Alex"),
                Filters.eq("age", 30),
                Filters.and(
                    Filters.eq("name", "Bob"),
                    Filters.lte("age", 50)
                )
            ),
            this.resolveMethod("findByCustom", "Alex", 30, "Bob", 50)
        );
    }

    public static class TestRepository {

        void findByName(String name) {}

        void findByNameAndAge(String name, Integer age) {}

        void findByNameAndAgeOrUsername(String name, Integer age, String username) {}

        void findByLtAge(Integer age) {}

        void findByNameAndLteAge(String name, Integer age) {}

        void findByGtAge(Integer age) {}

        void findByNameAndGteAge(String name, Integer age) {}

        @QuerySignature({"name", "|", "age", "|", "name", "&", "<=", "age"})
        void findByCustom(String name, Integer age, String otherName, Integer otherAge) {}
    }

    private Bson resolveMethod(String methodName, Object... args) {
        final Class<?>[] argsTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
        final Method method = SneakyThrows.supply(() -> TestRepository.class.getDeclaredMethod(methodName, argsTypes));
        final Query query = QueryImpl.parse(method).get();
        return query.resolve(args);
    }
}