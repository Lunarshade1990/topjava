package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int DAY_1_ID = START_SEQ + 3;
    public static final int DAY_2_ID = START_SEQ + 4;
    public static final int NOT_FOUND = 999;
    public static final LocalDateTime DATE_TIME_1 = LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0);
    public static final LocalDateTime DATE_TIME_2 = LocalDateTime.of(2023, Month.JANUARY, 31, 10, 0);
    public static final LocalDate DATE_1 = DATE_TIME_1.toLocalDate();
    public static final LocalDate DATE_2 = DATE_TIME_2.toLocalDate();
    public static final LocalDate EMPTY_DATE = LocalDate.of(1800, Month.DECEMBER, 1);

    public static final Meal day1 = new Meal(DAY_1_ID, DATE_TIME_1, "Завтрак", 500);
    public static final Meal day2 = new Meal(DAY_2_ID, DATE_TIME_2, "Завтрак", 1000);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, 2, 23, 12, 0), "new meal", 450);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(day1);
        updatedMeal.setDescription("new description");
        updatedMeal.setCalories(300);
        updatedMeal.setDateTime(LocalDateTime.of(2023, Month.JANUARY, 31, 21, 0));
        return updatedMeal;
    }

    public static Meal getDuplicateDate() {
        return new Meal(null,
                DATE_TIME_1,
                "new meal",
                350);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

}
