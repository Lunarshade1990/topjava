package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeFilter;

import java.util.Collection;

// TODO add userId
public interface MealRepository extends BaseRepository<Meal> {
    Collection<Meal> getAllByUserId(int userId);
    Collection<Meal> getAllByUserIdAndDateAndTime(int userId, DateTimeFilter filter);
}
