package ru.javawebinar.topjava.repository;

import java.util.Collection;

public interface BaseRepository<T> {
    // null if updated meal does not belong to userId
    T save(T meal);

    // false if meal does not belong to userId
    boolean delete(int id);

    // null if meal does not belong to userId
    T get(int id);

    // ORDERED dateTime desc
    Collection<T> getAll();
}
