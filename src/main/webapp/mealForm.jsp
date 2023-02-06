<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Meals</title>
    <link href="${pageContext.request.contextPath}/css/mealForm.css" rel="stylesheet" type="text/css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form class="meal-form" method="post" action="${pageContext.request.contextPath}/meals" name="meal">
    <c:if test="${meal.id != 0}">
        <div class="meal-id">
            <label for="id">DateTime: </label>
            <input type="number" id="id" name="id" value="${meal.id}"/>"/><br/>
        </div>
    </c:if>

    <div>
        <label for="datetime">DateTime: </label>
        <input type="datetime-local" id="datetime" name="datetime" value="${meal.dateTime}"/><br/>
    </div>
    <div>
        <label for="description">Description: </label>
        <input type="text" id="description" name="description" value="${meal.description}"/><br/>
    </div>
    <div>
        <label for="calories">Calories: </label>
        <input type="number" min="0" id="calories" name="calories" value="${meal.calories}"/><br/>
    </div>
    <div class="button-block">
        <button type="submit">Save</button>
        <a href="${pageContext.request.contextPath}/meals">Cancel</a>
    </div>
</form>
</body>
</html>
