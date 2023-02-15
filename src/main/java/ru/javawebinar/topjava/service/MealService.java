package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        checkAuthenticatedUser(meal.getUserId());
        return repository.save(meal);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        Meal meal = checkNotFoundWithId(repository.get(id), id);
        checkAuthenticatedUser(meal.getUserId());
        return meal;
    }

    public List<Meal> getAll() {
        return new ArrayList<>(repository.getAll());
    }

    public List<Meal> getAllByUserId(int userId) {
        return new ArrayList<>(repository.getAllByUserId(userId));
    }

    public List<Meal> getAllByUserIdAndDates(int userId, LocalDate startDate, LocalDate endDate) {
       return new ArrayList<>(repository.getAllByUserIdAndDateAndTime(userId, startDate, endDate));
    }

    public void update(Meal meal, int id) {
        get(meal.getId());
        assureIdConsistent(meal, id);
        checkNotFoundWithId(repository.save(meal), meal.getId());
    }

}