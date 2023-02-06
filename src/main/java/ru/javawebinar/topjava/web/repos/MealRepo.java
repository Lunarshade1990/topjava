package ru.javawebinar.topjava.web.repos;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepo {
    Meal save(Meal newMeal);
    Meal delete(long id);
    Meal update(Meal meal);
    Meal getById(long id);
    List<Meal> getAll();
}
