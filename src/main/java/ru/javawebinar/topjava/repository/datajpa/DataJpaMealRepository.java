package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_DATE = Sort.by(Sort.Direction.DESC, "dateTime");

    private final CrudMealRepository mealRepository;
    private final CrudUserRepository userRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository userRepository) {
        this.mealRepository = crudRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        meal.setUser(userRepository.getReferenceById(userId));
        if (meal.isNew()) {
            return mealRepository.save(meal);
        }
        return get(meal.id(), userId) == null ? null : mealRepository.save(meal);

    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return mealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = mealRepository.findById(id).orElse(null);
        return meal != null && meal.getUser().getId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepository.findAllByUserId(userId, SORT_DATE);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return mealRepository.getBetweenDates(userId, startDateTime, endDateTime);
    }


    @Transactional
    public Meal getWithUser(int id, int userId) {
        return mealRepository.getMealWithUser(id, userId);
    }
}
