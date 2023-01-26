package ru.javawebinar.topjava.util.mealfilter;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalTime;
import java.util.*;

public class MealFilterByCycles extends MealFilterInt{


    public MealFilterByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        super(meals, startTime, endTime, caloriesPerDay);
    }

    @Override
    public List<UserMealWithExcess> getMealsFilteredByTimeWithExcess() {
        List<UserMeal> filteredByTimeUserMeals = getMealsFilteredByTimeByCycle();
        if (filteredByTimeUserMeals.isEmpty()) {
            return Collections.emptyList();
        }
        return userMealsWithExcessesByCycle(meals, caloriesPerDay, filteredByTimeUserMeals);
    }

    private List<UserMeal> getMealsFilteredByTimeByCycle() {
        List<UserMeal> filteredUserMeals = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isInTimeRange(startTime, endTime, meal)) {
                filteredUserMeals.add(meal);
            }
        }
        return filteredUserMeals;
    }

    private List<UserMealWithExcess> userMealsWithExcessesByCycle(List<UserMeal> meals, int caloriesPerDay, List<UserMeal> userMeals) {
        Map<String, Integer> totalCalories = getTotalCalories(meals);
        return getUserMealWithExcesses(caloriesPerDay, userMeals, totalCalories);
    }

    private Map<String, Integer> getTotalCalories(List<UserMeal> meals) {
        Map<String, Integer> totalCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            String date = mealDateToString(meal);
            totalCalories.merge(date, meal.getCalories(), Integer::sum);
        }
        return totalCalories;
    }

    private List<UserMealWithExcess> getUserMealWithExcesses(int caloriesPerDay, List<UserMeal> filteredUserMeals, Map<String, Integer> totalCalories) {
        List<UserMealWithExcess> userMealsWithExcesses = new ArrayList<>();
        for (UserMeal meal : filteredUserMeals) {
            boolean excess = isExcess(caloriesPerDay, totalCalories, meal);
            UserMealWithExcess mealWithExcess = new UserMealWithExcess(meal, excess);
            userMealsWithExcesses.add(mealWithExcess);
        }
        return userMealsWithExcesses;
    }
}
