package ru.javawebinar.topjava.web.repos;

import java.time.LocalDateTime;
import java.time.Month;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import ru.javawebinar.topjava.model.Meal;

public class MealRepoInMemory implements MealRepoInt {
    private final static List<Meal> meals = new CopyOnWriteArrayList<>();
    private static long counter = 0L;

    public MealRepoInMemory() {
        meals.add(new Meal(1L, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(2L, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(3L, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(4L, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(5L, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(6L, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(7L, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public Meal create(Meal newMeal) {
        synchronized (meals) {
            newMeal.setId(counter++);
            meals.add(newMeal);

            return newMeal;
        }
    }

    @Override
    public boolean delete(long id) {
        return meals.removeIf(meal -> meal.getId() == id);
    }

    @Override
    public Meal update(Meal updatedMeal) {
        synchronized (meals) {
            Meal meal = getById(updatedMeal.getId());

            meal.setCalories(updatedMeal.getCalories());
            meal.setDateTime(updatedMeal.getDateTime());
            meal.setDescription(updatedMeal.getDescription());

            return meal;
        }
    }

    @Override
    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public Meal getById(long id) {
        return meals.stream()
                .filter(meal -> meal.getId() == id)
                .findFirst()
                .get();
    }
}


