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
        Map<LocalDate, Integer> totalCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            totalCalories.merge(getMealDate(meal), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> userMealsWithExcesses = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isTimeBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                userMealsWithExcesses.add(getUserMealWithExcess(meal, totalCalories, caloriesPerDay));
            }
        }
        return userMealsWithExcesses;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCalories = meals.stream()
                .collect(Collectors.groupingBy(UserMealsUtil::getMealDate,
                        Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(meal -> isTimeBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> getUserMealWithExcess(meal, totalCalories, caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static UserMealWithExcess getUserMealWithExcess(UserMeal meal, Map<LocalDate, Integer> totalCalories, int caloriesPerDay) {
        boolean excess = totalCalories.get(getMealDate(meal)) > caloriesPerDay;
        return new UserMealWithExcess(
                meal.getDateTime(),
                meal.getDescription(),
                meal.getCalories(),
                excess);
    }

    private static LocalDate getMealDate(UserMeal meal) {
        return meal.getDateTime().toLocalDate();
    }
}
