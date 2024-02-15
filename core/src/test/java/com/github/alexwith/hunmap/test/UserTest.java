package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.entity.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @BeforeAll
    public static void setup() {
        Humap.get().connect("mongodb://localhost:27017", "com/github/alexwith/humap/test");
    }

    @Test
    @Order(1)
    public void createTest() {
        final User user = Entity.create(new User("Alex", 10));
    }
}
