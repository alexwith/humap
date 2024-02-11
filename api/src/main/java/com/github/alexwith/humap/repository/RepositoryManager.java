package com.github.alexwith.humap.repository;

import com.github.alexwith.humap.entity.IdEntity;

public interface RepositoryManager {

    <K, T extends IdEntity<K>, U extends Repository<K, T>> U get(Class<U> repositoryClass);
}
