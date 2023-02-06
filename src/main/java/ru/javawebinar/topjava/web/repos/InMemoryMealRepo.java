package ru.javawebinar.topjava.web.repos;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealRepo implements MealRepo {
    private static final Logger log = getLogger(InMemoryMealRepo.class);
    private final Map<Long, Meal> meals = new ConcurrentHashMap<>();
    private long counter;
    private final ReentrantLock saveLock = new ReentrantLock();

    public InMemoryMealRepo() {
        MealsUtil.getMeals().forEach(meal -> {
            Meal newMeal = new Meal();
            newMeal.setDateTime(meal.getDateTime());
            newMeal.setCalories(meal.getCalories());
            newMeal.setDescription(meal.getDescription());
            save(newMeal);
        });
    }

    @Override
    public Meal save(Meal newMeal) {
        saveLock.lock();
        newMeal.setId(++counter);
        meals.put(counter, newMeal);
        saveLock.unlock();
        log.info("Meal was created: {}", newMeal);
        return newMeal;
    }

    @Override
    public Meal delete(long id) {
        Meal meal = meals.remove(id);
        log.info("Meal {} was deleted", meal);
        return meal;
    }

    @Override
    public Meal update(Meal updatedMeal) {
        synchronized (meals) {
            Meal meal = getById(updatedMeal.getId());

            meal.setCalories(updatedMeal.getCalories());
            meal.setDateTime(updatedMeal.getDateTime());
            meal.setDescription(updatedMeal.getDescription());

            log.info("Meal was updated: {}", meal);
            return meal;
        }
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getById(long id) {
        return meals.get(id);
    }
}


