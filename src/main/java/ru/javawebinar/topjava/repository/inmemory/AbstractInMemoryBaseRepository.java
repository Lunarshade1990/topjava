package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.repository.BaseRepository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AbstractInMemoryBaseRepository<T extends AbstractBaseEntity> implements BaseRepository<T> {
    final Map<Integer, T> repository = new ConcurrentHashMap<>();
    final AtomicInteger counter = new AtomicInteger(0);
    
    public T save(T entity) {
        if (entity.isNew()) {
            entity.setId(counter.incrementAndGet());
            repository.put(entity.getId(), entity);
            return entity;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(entity.getId(), (id, oldEntity) -> entity);
    }

    public boolean delete(int id) {
        return repository.remove(id) != null;
    }

    public T get(int id) {
        return repository.get(id);
    }

    public Collection<T> getAll() {
        return repository.values();
    }
}
