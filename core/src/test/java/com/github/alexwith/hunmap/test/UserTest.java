package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.Humap;
import com.github.alexwith.humap.dirtytracking.DirtyTracker;
import com.github.alexwith.humap.instance.Instances;
import com.github.alexwith.humap.proxy.Proxy;
import com.github.alexwith.humap.proxy.ProxyCreationContextImpl;
import com.github.alexwith.humap.proxy.ProxyFactoryImpl;
import com.github.alexwith.humap.type.ParamedTypeImpl;
import java.util.ArrayList;
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
        //final User user = Entity.create(new User("Alex", 10));

        final User user = Instances.get(ProxyFactoryImpl.class).proxy(ProxyCreationContextImpl.of((builder) -> builder
            .origin(new User("Alex", 10, new ArrayList<>()))
            .type(new ParamedTypeImpl(User.class))
        ));

        user.setInternalIdCounter(30L);
        System.out.println("whats good: " + user.getInternalIdCounter());

        System.out.println(user.getInternalId());

        //System.out.println("user: " + user);
        //System.out.println("test: " + user.getName());

        //user.setName("Bob");

        //System.out.println("name: " + user.getName());

        System.out.println("user: " + user.getHistory().getClass());

        final Proxy proxy = Proxy.asProxy(user);
        final DirtyTracker dirtyTracker = proxy.getDirtyTracker();
        System.out.println(dirtyTracker.getDirty());
    }
}
