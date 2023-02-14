package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.DateTimeFilter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

@Repository
public class InMemoryMealRepository extends AbstractInMemorySortedRepository<Meal> implements MealRepository {

    public InMemoryMealRepository() {
        defaultComparator = Comparator.comparing(Meal::getDateTime).reversed();
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Collection<Meal> getAllByUserId(int userId) {
        return repository
                .values()
                .stream()
                .filter(meal -> meal.belongsUser(userId))
                .sorted(defaultComparator)
                .collect(Collectors.toList());
    }

    public Collection<Meal> getAllByUserIdAndDateAndTime(int userId, DateTimeFilter filter) {
        LocalDate startDate = filter.getStartDate() == null ? LocalDate.MIN : filter.getStartDate();
        LocalDate endDate = filter.getEndDate() == null ? LocalDate.MAX : filter.getEndDate();
        LocalTime startTime = filter.getStartTime() == null ? LocalTime.MIN : filter.getStartTime();
        LocalTime endTime = filter.getEndTime() == null ? LocalTime.MAX : filter.getEndTime();

        return repository
                .values()
                .stream()
                .filter(meal -> isBetween(meal.getDate(), startDate, endDate, false)
                        && isBetween(meal.getTime(), startTime, endTime, true)
                        && meal.belongsUser(userId))
                .collect(Collectors.toList());
    }
}

