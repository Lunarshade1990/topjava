package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;
import static ru.javawebinar.topjava.util.MealsUtil.getMeals;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("received /meals GET request");
        List<Meal> meals = getMeals();
        List<MealTo> mealTos = filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("meals", mealTos);
        request.getRequestDispatcher("/meals.jsp")
                .forward(request, response);
    }
}
