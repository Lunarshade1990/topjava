package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal SET dateTime=:dateTime, description=:description, calories=:calories " +
                "WHERE user.id=:userId AND id=:id"),
        @NamedQuery(name = Meal.GET, query = "SELECT m FROM Meal m " +
                "WHERE m.id=:id AND m.user.id=:userId ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT m FROM Meal m " +
                "WHERE m.user.id=:userId ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.ALL_BETWEEN_DATES, query = "SELECT m FROM Meal m " +
                "WHERE m.user.id=:userId AND m.dateTime >=:startDate AND m.dateTime <:endDate ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m " +
                "WHERE m.id=:id AND m.user.id=:userId")
})
@Entity
@Table(name = "meal", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date_time"}))
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "Meal.delete";
    public static final String UPDATE = "Meal.update";
    public static final String GET = "Meal.get";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String ALL_BETWEEN_DATES = "Meal.getAllBetweenDates";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @Length(min = 2, max = 120)
    private String description;

    @Column(name = "calories", nullable = false)
    @Min(10) @Max(5000)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
