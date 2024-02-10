package com.github.alexwith.hunmap.test;

import com.github.alexwith.humap.repository.Repository;
import java.util.UUID;

public interface UserRepository extends Repository<UUID, User> {

    User findByName(String name);
}
