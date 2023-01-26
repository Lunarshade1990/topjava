package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.util.mealfilter.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * Чтобы код было удобнее читать, выполнение циклами и стримами было разбито на 2 отдельных класса
 * {@link MealFilterByStream} и {@link MealFilterByCycles}, наследующихся от абстрактного класса
 * {@link ru.javawebinar.topjava.util.mealfilter.MealFilterInt}, в который вынесено несколько общих методов.
 * Наследники должны имплементировать метод {@link MealFilterInt#getMealsFilteredByTimeWithExcess()}
 */
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

        List<UserMealWithExcess> mealsByCycles = new MealFilterByCycles(meals, LocalTime.of(8, 0), LocalTime.of(13, 0), 2000)
                .getMealsFilteredByTimeWithExcess();
        List<UserMealWithExcess> mealsByStream = new MealFilterByStream(meals, LocalTime.of(8, 0), LocalTime.of(13, 0), 2000)
                .getMealsFilteredByTimeWithExcess();

        mealsByCycles.forEach(System.out::println);
        System.out.println();
        mealsByStream.forEach(System.out::println);
    }
}
