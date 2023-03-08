package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Modifying
    @Query(name = Meal.DELETE)
    int delete(@Param("id") int id, @Param("userId") int userId);

    List<Meal> findAllByUserId(int userId, Sort sort);

    @Query(name = Meal.GET_BETWEEN)
    List<Meal> getBetweenDates(@Param("userId") int userId,
                               @Param("startDateTime") LocalDateTime startDateTime,
                               @Param("endDateTime") LocalDateTime endDateTime);

    @Query("SELECT m FROM Meal m INNER JOIN FETCH m.user where m.id =:id and m.user.id =:userId")
    Meal getMealWithUser(@Param("id") int id, @Param("userId") int userId);

}
