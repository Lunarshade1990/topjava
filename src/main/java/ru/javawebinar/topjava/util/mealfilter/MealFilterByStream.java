package ru.javawebinar.topjava.util.mealfilter;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealFilterByStream extends MealFilterInt {

    public MealFilterByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        super(meals, startTime, endTime, caloriesPerDay);
    }

    @Override
    public List<UserMealWithExcess> getMealsFilteredByTimeWithExcess() {
        List<UserMeal> filteredByTimeUserMeals = getMealsFilteredByTimeByStream();
        if (filteredByTimeUserMeals.isEmpty()) {
            return Collections.emptyList();
        }
        return userMealsWithExcessesByStream(meals, caloriesPerDay, filteredByTimeUserMeals);
    }

    private List<UserMeal> getMealsFilteredByTimeByStream() {
        return meals.stream()
                .filter(timeRangePredicate(startTime, endTime))
                .collect(Collectors.toList());
    }

    private Predicate<UserMeal> timeRangePredicate(LocalTime startTime, LocalTime endTime) {
        return userMeal -> isInTimeRange(startTime, endTime, userMeal);
    }

    private List<UserMealWithExcess> userMealsWithExcessesByStream(List<UserMeal> meals, int caloriesPerDay, List<UserMeal> filteredMeals) {
        Map<String, Integer> totalCaloriesPerDay = getTotalCaloriesByStream(meals);
        return filteredMeals.stream()
                .map(getUserMealWithExcess(caloriesPerDay, totalCaloriesPerDay))
                .collect(Collectors.toList());
    }

    private Map<String, Integer> getTotalCaloriesByStream(List<UserMeal> meals) {
        return meals.stream()
                .collect(Collectors.groupingBy(MealFilterInt::mealDateToString,
                        Collectors.summingInt(UserMeal::getCalories)));
    }

    private Function<UserMeal, UserMealWithExcess> getUserMealWithExcess(int caloriesPerDay, Map<String, Integer> totalCalories) {
        return userMeal -> new UserMealWithExcess(userMeal, isExcess(caloriesPerDay, totalCalories, userMeal));
    }
}
