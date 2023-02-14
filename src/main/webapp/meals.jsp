<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
        .filter-container {
            width: 534px;
            display: flex;
            justify-content: space-between;
        }
        .filter-container div {
            width: 30%;
            display: flex;
            flex-direction: column;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <div>
        <form action="${pageContext.request.contextPath}/meals?action=create" method="get">
<%--            <jsp:useBean id="dateTimeFilter" type="ru.javawebinar.topjava.util.DateTimeFilter"/>--%>
            <div class="filter-container">
                <div>
                    <label for="startDate">От даты (включая)</label>
                    <input id="startDate" name="startDate" type="date"
                           value="${dateTimeFilter.startDate == null ? "" : dateTimeFilter.startDate}"/>

                    <label for="endDate">До даты (включая)</label>
                    <input id="endDate"  name="endDate" type="date"
                           value="${dateTimeFilter.endDate == null ? "" : dateTimeFilter.endDate}"/>
                </div>
                <div>
                    <label for="startTime">От времени (включая)</label>
                    <input id="startTime" name="startTime" type="time"
                           value="${dateTimeFilter.startTime == null ? "" : dateTimeFilter.startTime}"/>

                    <label for="endTime">До времени (исключая)</label>
                    <input id="endTime" name="endTime" type="time"
                           value="${dateTimeFilter.endTime == null ? "" : dateTimeFilter.endTime}"/>
                </div>
            </div>
            <div>
                <button type="submit">Отфильтровать</button>
                <a href="${pageContext.request.contextPath}/meals">Сбросить</a>
            </div>
        </form>
    </div>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${requestScope.meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>