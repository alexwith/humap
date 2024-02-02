package com.github.alexwith.humap.test;

import com.github.alexwith.humap.repository.Repository;

public class EntityTest {

    public void test() {
        final TestRepository repository1 = Repository.get(TestRepository.class);

        Repository.get(TestRepository.class, (repository2) -> {

        });
    }
}
