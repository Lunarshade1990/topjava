package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isTimeBetweenHalfOpen;

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
        Map<LocalDate, Integer> totalCalories = getTotalCalories(meals);
        List<UserMealWithExcess> userMealsWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isInTimeRange(startTime, endTime, meal)) {
                boolean excess = isExcess(caloriesPerDay, totalCalories, meal);
                userMealsWithExcesses.add(new UserMealWithExcess(meal, excess));
            }
        }
        return userMealsWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCalories = getTotalCaloriesByStream(meals);
        return meals.stream()
                .filter(meal -> isInTimeRange(startTime, endTime, meal))
                .map(meal -> new UserMealWithExcess(meal, isExcess(caloriesPerDay, totalCalories, meal)))
                .collect(Collectors.toList());
    }

    private static Map<LocalDate, Integer> getTotalCalories(List<UserMeal> meals) {
        Map<LocalDate, Integer> totalCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            totalCalories.merge(getMealDate(meal), meal.getCalories(), Integer::sum);
        }
        return totalCalories;
    }

    private static boolean isExcess(int caloriesPerDay, Map<LocalDate, Integer> totalCalories, UserMeal meal) {
        return totalCalories.get(getMealDate(meal)) > caloriesPerDay;
    }

    private static boolean isInTimeRange(LocalTime startTime, LocalTime endTime, UserMeal meal) {
        LocalTime mealTime = getMealTime(meal);
        return isTimeBetweenHalfOpen(mealTime, startTime, endTime);
    }

    private static Map<LocalDate, Integer> getTotalCaloriesByStream(List<UserMeal> meals) {
        return meals.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getMealDate,
                        Collectors.summingInt(UserMeal::getCalories)));
    }
    
    private static LocalTime getMealTime(UserMeal meal) {
        return meal.getDateTime().toLocalTime();
    }

    private static LocalDate getMealDate(UserMeal meal) {
        return meal.getDateTime().toLocalDate();
    }
}
