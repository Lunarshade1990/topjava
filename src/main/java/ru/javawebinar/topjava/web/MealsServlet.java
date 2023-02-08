package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.web.repos.InMemoryMealRepo;
import ru.javawebinar.topjava.web.repos.MealRepo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealsServlet extends HttpServlet {
    private static final Logger log = getLogger(MealsServlet.class);
    private MealRepo mealRepo;

    @Override
    public void init() throws ServletException {
        mealRepo = new InMemoryMealRepo();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "delete": {
                long id = getId(request);
                log.info("received /meals {} request with ID {}", action.toUpperCase(), id);
                mealRepo.delete(id);
                response.sendRedirect(request.getContextPath() + "/meals");
                break;
            }
            case "update": {
                long id = getId(request);
                log.info("received /meals {} request with ID {}", action.toUpperCase(), id);
                Meal meal = mealRepo.getById(id);
                forwardToMealForm(meal, action, request, response);
                break;
            }
            case "create": {
                forwardToMealForm(new Meal(), action, request, response);
                break;
            }
            default: {
                int CALORIES_PER_DAY = 2000;
                List<MealTo> mealTos = filteredByStreams(mealRepo.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
                request.setAttribute("meals", mealTos);
                request.getRequestDispatcher("/meals.jsp")
                        .forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String strId = request.getParameter("id");
        Meal meal = createMeal(request);
        if (strId == null) {
            mealRepo.create(meal);
        } else {
            meal.setId(Long.parseLong(strId));
            mealRepo.update(meal);
        }
        response.sendRedirect(request.getContextPath() + "/meals");
    }

    private void forwardToMealForm(Meal meal, String action, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("received /meals {} request, redirect to mealForm.jsp", action.toUpperCase());
        request.setAttribute("meal", meal);
        RequestDispatcher view = request.getRequestDispatcher("/mealForm.jsp");
        view.forward(request, response);
    }

    private Meal createMeal(HttpServletRequest request) {
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("datetime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal();
        meal.setDescription(description);
        meal.setDateTime(localDateTime);
        meal.setCalories(calories);
        return meal;
    }

    private long getId(HttpServletRequest request) {
        return Long.parseLong(request.getParameter("id"));
    }


}
