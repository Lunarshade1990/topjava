package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeFilter;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal Meal) {
        return repository.save(Meal);
    }

    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(repository.getAll());
    }

    public List<Meal> getAllByUserId(int userId) {
        return new ArrayList<>(repository.getAllByUserId(userId));
    }

    public List<Meal> getAllByUserIdAndDateAndTime(int userId, DateTimeFilter filter) {
       return new ArrayList<>(repository.getAllByUserIdAndDateAndTime(userId, filter));
    }

    public void update(Meal Meal) {
        checkNotFoundWithId(repository.save(Meal), Meal.getId());
    }

}