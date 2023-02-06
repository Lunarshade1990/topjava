package ru.javawebinar.topjava.web.repos;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealRepoInMemory implements MealRepoInt {
    private static final Logger log = getLogger(MealRepoInMemory.class);
    private final static List<Meal> meals = new CopyOnWriteArrayList<>();
    private static long counter = 8L;

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
            log.info("Meal was created: {}", newMeal);
            return newMeal;
        }
    }

    @Override
    public boolean delete(long id) {
        boolean wasRemoved = meals.removeIf(meal -> meal.getId() == id);
        if (wasRemoved) log.info("Meal with id {} was deleted", id);
        return wasRemoved;
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
        return meals;
    }

    @Override
    public Meal getById(long id) {
        Meal meal = meals.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElseThrow();
        log.info("Meal was found: {}", meal);
        return meal;
    }
}


