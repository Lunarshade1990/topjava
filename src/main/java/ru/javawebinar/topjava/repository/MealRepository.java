package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.List;

// TODO add userId
public interface MealRepository extends BaseRepository<Meal> {
    List<Meal> getAllByUserId(int userId);
    List<Meal> getAllByUserIdAndDateAndTime(int userId, LocalDate startDate, LocalDate endDate);
}
