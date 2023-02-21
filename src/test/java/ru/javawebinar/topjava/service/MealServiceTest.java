package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newUser = getNew();
        newUser.setId(newId);
        assertMatch(created, newUser);
        assertMatch(service.get(newId, USER_ID), newUser);
    }

    @Test
    public void duplicateDateCreate() {
        assertThrows(DataAccessException.class, () ->service.create(getDuplicateDate(), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(TEST_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(TEST_ID, USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(TEST_ID, USER_ID);
        assertMatch(meal, testMeal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void updateDeletedNotFound() {
        service.delete(TEST_ID, USER_ID);
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, userMeals);
    }

    @Test
    public void  getBetweenInclusive() {
        List<Meal> all = service.getBetweenInclusive(TEST_DATE, TEST_DATE, USER_ID);
        assertMatch(all, userMealsFor31January);
    }

    @Test
    public void  getBetweenInclusiveFewDates() {
        List<Meal> all = service.getBetweenInclusive(TEST_DATE.minusDays(1), TEST_DATE, USER_ID);
        assertMatch(all, userMeals);
    }

    @Test
    public void  getBetweenInclusiveEmpty() {
        List<Meal> all = service.getBetweenInclusive(EMPTY_DATE, EMPTY_DATE, USER_ID);
        assertThat(all).isEmpty();
    }
}