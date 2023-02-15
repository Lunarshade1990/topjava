package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

@Repository
public class InMemoryMealRepository extends AbstractInMemorySortedRepository<Meal> implements MealRepository {

    public InMemoryMealRepository() {
        defaultComparator = Comparator.comparing(Meal::getDateTime).reversed();
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public List<Meal> getAllByUserId(int userId) {
        return repository
                .values()
                .stream()
                .filter(meal -> meal.belongsUser(userId))
                .sorted(defaultComparator)
                .collect(Collectors.toList());
    }

    public List<Meal> getAllByUserIdAndDateAndTime(int userId, LocalDate startDate, LocalDate endDate) {
        LocalDate sDate = startDate == null ? LocalDate.MIN : startDate;
        LocalDate eDate = startDate == null ? LocalDate.MAX : endDate;

        return repository
                .values()
                .stream()
                .filter(meal -> isBetween(meal.getDate(), sDate, eDate, false))
                .filter(meal -> meal.belongsUser(userId))
                .collect(Collectors.toList());
    }
}

