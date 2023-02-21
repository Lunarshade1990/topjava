package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final int MEAL_ID = 100009;

    private final static List<Meal> meals = Arrays.asList(
            new Meal(100009, LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0), "Ужин", 410),
            new Meal(100008, LocalDateTime.of(2023, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(100007, LocalDateTime.of(2023, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(100005, LocalDateTime.of(2023, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(100004, LocalDateTime.of(2023, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(100003, LocalDateTime.of(2023, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(100006, LocalDateTime.of(2023, Month.JANUARY, 30, 0, 0), "Еда на граничное значение", 100));
    private final static Comparator<Meal> dateComparator = Comparator.comparing(Meal::getDateTime).reversed();
    public final static LocalDate TEST_DATE = LocalDate.of(2023, Month.JANUARY, 31);
    public final static LocalDate EMPTY_DATE = LocalDate.of(2020, Month.JANUARY, 31);
    public static final int TEST_ID = 100009;
    public static final int NOT_FOUND = 999;

    public static final Meal testMeal = new Meal(100009, LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final List<Meal> userMeals = meals.stream()
            .sorted(dateComparator)
            .collect(Collectors.toList());

    public static final List<Meal> userMealsFor31January = meals.stream()
            .filter(m -> m.getDate().isEqual(TEST_DATE))
            .sorted(dateComparator)
            .collect(Collectors.toList());

    public static final Meal meal = new Meal(MEAL_ID, LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2023, 2, 23, 12, 0), "new meal", 450);
    }

    public static Meal getUpdated() {
        Meal updatedMeal = new Meal(meal);
        updatedMeal.setDescription("new description");
        updatedMeal.setCalories(300);
        updatedMeal.setDateTime(LocalDateTime.of(2023, Month.JANUARY, 31, 21, 0));
        return updatedMeal;
    }

    public static Meal getDuplicateDate() {
        return new Meal(null,
                LocalDateTime.of(2023, Month.JANUARY, 31, 20, 0),
                "new meal",
                350);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

}
