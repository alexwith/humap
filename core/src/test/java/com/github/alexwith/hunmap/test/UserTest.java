package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.Humap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @BeforeAll
    public static void setup() {
        Humap.get().connect("mongodb://localhost:27017", "com/github/alexwith/humap/test");
    }
}
