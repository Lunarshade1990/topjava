package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static ru.javawebinar.topjava.util.TimeUtil.*;

/**
 * Комментарий к HW0
 * <br>Логика работы методов {@link #filteredByCycles(List, LocalTime, LocalTime, int)} и {@link #filteredByStreams(List, LocalTime, LocalTime, int)}:
 * <ol>
 *     <li>Офильтровать еду по заданному временному интервалу. Циклом {@link #getMealsFilteredByTimeByCycle(List, LocalTime, LocalTime)}
 *     и стримом {@link #getMealsFilteredByTimeByStream(List, LocalTime, LocalTime)}</li>
 *     <li>Если в это время ничего нет, возвращаем пустой массив, чтобы не делать лишние действия дальше</li>
 *     <li>Просуммировать калории по дням, добавив в Map. В качестве ключа используем строковое представление даты. Реализация с помощью цикла
 *     {@link #getTotalCalories(List)}, с помощью стрима {@link #getTotalCaloriesByStream(List)}, </li>
 *     <li>Для каждого объекта {@link UserMeal} создаём новый объект {@link UserMealWithExcess},
 *     проверяя, превышена ли в заданный день калорийность, обращаясь к созданной ранее мэпе по дате</li>
 *     <li>вернуть получившийся список</li>
 * </ol>
 * Принимая сложность добавления в HashMap и обращения по ключу за O(1), сложность решения получается O(N)
 **/
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsByCycles = filteredByCycles(meals, LocalTime.of(8, 0), LocalTime.of(13, 0), 2000);
        List<UserMealWithExcess> mealsByStream = filteredByStreams(meals, LocalTime.of(8, 0), LocalTime.of(13, 0), 2000);
        mealsByCycles.forEach(System.out::println);
        System.out.println();
        mealsByStream.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> filteredUserMeals = getMealsFilteredByTimeByCycle(meals, startTime, endTime);
        if (filteredUserMeals.isEmpty()) {
            return Collections.emptyList();
        }
        return userMealsWithExcessesByCycle(meals, caloriesPerDay, filteredUserMeals);
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMeal> filteredUserMeals = getMealsFilteredByTimeByStream(meals, startTime, endTime);
        if (filteredUserMeals.isEmpty()) {
            return Collections.emptyList();
        }
        return userMealsWithExcessesByStream(meals, caloriesPerDay, filteredUserMeals);
    }

    private static List<UserMeal> getMealsFilteredByTimeByCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime) {
        List<UserMeal> filteredUserMeals = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isInTimeRange(startTime, endTime, meal)) {
                filteredUserMeals.add(meal);
            }
        }
        return filteredUserMeals;
    }

    private static List<UserMealWithExcess> userMealsWithExcessesByCycle(List<UserMeal> meals, int caloriesPerDay, List<UserMeal> filteredUserMeals) {
        Map<String, Integer> totalCalories = getTotalCalories(meals);
        return getUserMealWithExcesses(caloriesPerDay, filteredUserMeals, totalCalories);
    }

    private static Map<String, Integer> getTotalCalories(List<UserMeal> meals) {
        Map<String, Integer> totalCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            String date = dateToString(meal);
            totalCalories.merge(date, meal.getCalories(), Integer::sum);
        }
        return totalCalories;
    }

    private static List<UserMealWithExcess> getUserMealWithExcesses(int caloriesPerDay, List<UserMeal> filteredUserMeals, Map<String, Integer> totalCalories) {
        List<UserMealWithExcess> userMealsWithExcesses = new ArrayList<>();
        for (UserMeal meal : filteredUserMeals) {
            boolean excess = isExcess(caloriesPerDay, totalCalories, meal);
            UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal, excess);
            userMealsWithExcesses.add(mealWithExcess);
        }
        return userMealsWithExcesses;
    }

    private static boolean isExcess(int caloriesPerDay, Map<String, Integer> totalCalories, UserMeal meal) {
        String date = dateToString(meal);
        return totalCalories.get(date) > caloriesPerDay;
    }

    private static boolean isInTimeRange(LocalTime startTime, LocalTime endTime, UserMeal meal) {
        LocalTime mealTime = getMealTime(meal);
        return isTimeBetweenHalfOpen(mealTime, startTime, endTime);
    }

    private static String dateToString(UserMeal meal) {
        return toLocaleDate(meal.getDateTime()).toString();
    }

    private static LocalTime getMealTime(UserMeal meal) {
        return toLocaleTime(meal.getDateTime());
    }

    private static List<UserMeal> getMealsFilteredByTimeByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime) {
        return meals.stream()
                .filter(timeRangePredicate(startTime, endTime))
                .collect(Collectors.toList());
    }

    private static Map<String, Integer> getTotalCaloriesByStream(List<UserMeal> meals) {
        return meals.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::dateToString,
                        Collectors.summingInt(UserMeal::getCalories)));
    }

    private static List<UserMealWithExcess> userMealsWithExcessesByStream(List<UserMeal> meals, int caloriesPerDay, List<UserMeal> filteredMeals) {
        Map<String, Integer> totalCalories = getTotalCaloriesByStream(meals);
        return filteredMeals.stream()
                .map(getUserMealWithExcess(caloriesPerDay, totalCalories))
                .collect(Collectors.toList());
    }

    private static Function<UserMeal, UserMealWithExcess> getUserMealWithExcess(int caloriesPerDay, Map<String, Integer> totalCalories) {
        return userMeal -> new UserMealWithExcess(userMeal, isExcess(caloriesPerDay, totalCalories, userMeal));
    }

    private static Predicate<UserMeal> timeRangePredicate(LocalTime startTime, LocalTime endTime) {
        return userMeal -> isInTimeRange(startTime, endTime, userMeal);
    }
}
