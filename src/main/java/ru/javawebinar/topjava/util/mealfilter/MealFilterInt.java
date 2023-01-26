package ru.javawebinar.topjava.util.mealfilter;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.topjava.util.TimeUtil.*;

/**
 * <br>Логика работы метода {@link #getMealsFilteredByTimeWithExcess()}:
 * <ol>
 *     <li>Офильтровать приёмы еды по заданному временному интервалу
 *     <li>Если в это время ничего нет, возвращаем пустой массив, чтобы не делать лишние действия дальше</li>
 *     <li>Просуммировать калории по дням, добавив в Map. В качестве ключа используем строковое представление даты</li>
 *     <li>Для каждого объекта {@link UserMeal} создаём новый объект {@link UserMealWithExcess},
 *     проверяя, превышена ли в заданный день калорийность, обращаясь к созданной ранее мэпе по дате</li>
 *     <li>вернуть получившийся список</li>
 * </ol>
 * Принимая среднюю сложность добавления в HashMap и обращения по ключу за O(1), сложность решения получается O(N)
 **/
public abstract class MealFilterInt {

    final List<UserMeal> meals;
    final LocalTime startTime;
    final LocalTime endTime;
    final int caloriesPerDay;

    public MealFilterInt(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        this.meals = meals;
        this.startTime = startTime;
        this.endTime = endTime;
        this.caloriesPerDay = caloriesPerDay;
    }

    public abstract List<UserMealWithExcess> getMealsFilteredByTimeWithExcess();

    static LocalTime getMealTime(UserMeal meal) {
        return toLocaleTime(meal.getDateTime());
    }

    static boolean isExcess(int caloriesPerDay, Map<String, Integer> totalCalories, UserMeal meal) {
        String date = mealDateToString(meal);
        return totalCalories.get(date) > caloriesPerDay;
    }

    static String mealDateToString(UserMeal meal) {
        return toLocaleDate(meal.getDateTime()).toString();
    }

    static boolean isInTimeRange(LocalTime startTime, LocalTime endTime, UserMeal meal) {
        LocalTime mealTime = getMealTime(meal);
        return isTimeBetweenHalfOpen(mealTime, startTime, endTime);
    }
}
