package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeFilter;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAllByUserId(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getAllByDateAndTime(DateTimeFilter filter) {
        log.info("getAllByDateAndTime");
        return MealsUtil.getTos(service.getAllByUserIdAndDateAndTime(authUserId(), filter), authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        Meal meal = service.get(id);
        checkAuthenticatedUser(meal.getUserId());
        return meal;
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        checkAuthenticatedUser(meal.getUserId());
        return service.create(meal);
    }

    public void delete(int id) {
        get(id);
        log.info("delete {}", id);
        service.delete(id);
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        checkAuthenticatedUser(meal.getUserId());
        service.update(meal);
    }
}