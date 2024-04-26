package com.github.alexwith.humap.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.annotation.Collection;
import com.github.alexwith.humap.annotation.EntityId;
import com.github.alexwith.humap.annotation.FindAll;
import com.github.alexwith.humap.annotation.Modifies;
import com.github.alexwith.humap.annotation.QuerySignature;
import com.github.alexwith.humap.entity.Entity;
import com.github.alexwith.humap.entity.IdEntity;
import com.github.alexwith.humap.repository.Repository;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserTest extends TestHumap {

    @BeforeAll
    public static void setup() {
        Humap.get().connect("mongodb://localhost:27017", "test");
    }

    @Test
    public void loadTest() {
        this.applyEntity(this.sampleUser(), (user) -> {
            final Pet pet = user.getPet();

            Repository.consume(UserRepository.class, (repository) -> {
                final User foundUser = repository.findById(user.getId());
                assertEquals(user.getId(), foundUser.getId());
                assertEquals(user.getName(), foundUser.getName());
                assertEquals(user.getScore(), foundUser.getScore());
                assertEquals(user.getHistory(), foundUser.getHistory());
                assertEquals(user.getGrades(), foundUser.getGrades());

                final Pet foundPet = foundUser.getPet();
                assertEquals(pet.getName(), foundPet.getName());
                assertEquals(pet.getAge(), foundPet.getAge());
            });
        });
    }

    private User sampleUser() {
        final User user = new User(UUID.randomUUID(), "Alex", 100, new ArrayList<>(), new HashMap<>(), null).proxy();
        user.getHistory().add("This is in the log!");
        user.getGrades().put("English", "A-");
        user.setPet(new Pet("Snoop Dog", 5));
        return user;
    }

    public interface UserRepository extends Repository<UUID, User> {

        User findByName(String name);

        @QuerySignature("name")
        CompletableFuture<User> findByNameAsync(String name);

        User findByNameAndLtScore(String name, int score);

        User findByNameAndScoreOrName(String name, int score, String otherName);

        @QuerySignature({"name", "|", "score"})
        User findBySpecialCase(String name, int score);

        @FindAll
        CompletableFuture<List<User>> findByGtScore(int score);

        default List<User> findSmallScores() {
            return this.findAll(Filters.lte("score", 100));
        }
    }

    @Collection("user")
    public static class User implements IdEntity<UUID> {
        private @EntityId UUID id;
        private String name;
        private int score;
        private List<String> history;
        private Map<String, String> grades;
        private Pet pet;

        public User(UUID id, String name, int score, List<String> history, Map<String, String> grades, Pet pet) {
            this.id = id;
            this.name = name;
            this.score = score;
            this.history = history;
            this.grades = grades;
            this.pet = pet;
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

        public Pet getPet() {
            return this.pet;
        }

        public void setPet(Pet pet) {
            this.pet = pet;
        }
    }

    public static class Pet implements Entity {
        private String name;
        private int age;

        public Pet(String name, int age) {
            this.name = name;
            this.age = age;
        }

        protected Pet() {}

        public String getName() {
            return this.name;
        }

        public int getAge() {
            return this.age;
        }

        @Modifies("age")
        public void modifyAge(UnaryOperator<Integer> modifier) {
            this.age = modifier.apply(this.age);
        }
    }
}
