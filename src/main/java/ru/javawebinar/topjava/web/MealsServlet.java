package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.web.repos.MealRepoInMemory;
import ru.javawebinar.topjava.web.repos.MealRepoInt;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);
    MealRepoInt mealRepo = new MealRepoInMemory();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        long id;
        switch (action) {
            case "delete" -> {
                id = Long.parseLong(request.getParameter("id"));
                mealRepo.delete(id);
                forwardToMeals(request, response);
            }
            case "update" -> {
                id = Long.parseLong(request.getParameter("id"));
                Meal meal = mealRepo.getById(id);
                forwardToMealForm(meal, action, request, response);
            }
            case "create" -> {
                forwardToMealForm(new Meal(), action, request, response);
            }
            default -> forwardToMeals(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        Meal meal = createMeal(request);
        if (id == 0) {
            mealRepo.create(meal);
        } else {
            meal.setId(id);
            mealRepo.update(meal);
        }
        forwardToMeals(request, response);
    }

    private void forwardToMeals(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealTos = filteredByStreams(mealRepo.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        request.setAttribute("meals", mealTos);
        request.getRequestDispatcher("/meals.jsp")
                .forward(request, response);
    }

    private void forwardToMealForm(Meal meal, String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meal", meal);
        request.setAttribute("action", action);
        request.getRequestDispatcher("/mealForm.jsp")
                .forward(request, response);
    }

    private Meal createMeal(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        Meal meal = new Meal();
        meal.setDescription(description);
        meal.setDateTime(localDateTime);
        meal.setCalories(calories);
        return meal;
    }


}
