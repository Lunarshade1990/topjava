package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.AbstractBaseEntity;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class AbstractInMemorySortedRepository<T extends AbstractBaseEntity>  extends AbstractInMemoryBaseRepository<T>{
    Comparator<T> defaultComparator = Comparator.comparing(T::getId);

    @Override
    public Collection<T> getAll() {
        return repository
                .values()
                .stream()
                .sorted(defaultComparator)
                .collect(Collectors.toList());
    }
}
