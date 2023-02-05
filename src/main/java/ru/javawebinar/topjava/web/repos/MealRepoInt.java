package ru.javawebinar.topjava.web.repos;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepoInt {
    Meal create(Meal newMeal);
    boolean delete(long id);
    Meal update(Meal meal);
    Meal getById(long id);
    List<Meal> getAll();
}
