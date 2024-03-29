package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.annotation.QuerySignature;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.repository.Repository;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @BeforeAll
    public static void setup() {
        Humap.get().connect("mongodb://localhost:27017", "test");
    }

    @Test
    @Order(1)
    public void createTest() {
        final User user = new User(UUID.randomUUID(), "Alex", 100, new ArrayList<>(), new HashMap<>()).proxy();

        user.getHistory().add("hello there");
        user.getHistory().add("whats gooddd");
        user.getGrades().put("English", "A+");

        user.save();

        //System.out.println("name: " + user.getName());

        /*final Proxy proxy = Proxy.asProxy(user);
        final DirtyTracker dirtyTracker = proxy.getDirtyTracker();
        System.out.println("dirty: " + dirtyTracker.getDirty());*/

        /*Repository.consume(UserRepository.class, (repository) -> {
            System.out.println("test: " + repository.findByName("Bob"));
        });*/
    }

    public interface UserRepository extends Repository<UUID, User> {

        User findByName(String name);

        User findByNameAndLtScore(String name, int score);

        User findByNameAndScoreOrName(String name, int score, String otherName);

        @QuerySignature({"name", "|", "score"})
        User findBySpecialCase(String name, int score);

        default List<User> findSmallScores() {
            return this.findAll(Filters.lte("score", 100));
        }
    }

    @Collection("user")
    public static class User implements IdEntity<UUID> {

        @EntityId
        private UUID id;

        private String name;

        private int score;

        private List<String> history;

        private Map<String, String> grades;

        public User(UUID id, String name, int score, List<String> history, Map<String, String> grades) {
            this.id = id;
            this.name = name;
            this.score = score;
            this.history = history;
            this.grades = grades;
        }

        protected User() {}

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

        public int getScore() {
            return this.score;
        }

        @Modifies("score")
        public void modifyScore(UnaryOperator<Integer> modifier) {
            this.score = modifier.apply(this.score);
        }

        public List<String> getHistory() {
            return this.history;
        }

        public Map<String, String> getGrades() {
            return this.grades;
        }
    }
}
