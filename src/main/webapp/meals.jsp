<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>Meals</title>
    <link href="${pageContext.request.contextPath}/css/meals.css" rel="stylesheet" type="text/css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:set var="rowcolor" value="green"/>
    <c:forEach var="meal" items="${ meals }">
        <c:if test="${meal.excess}">
            <c:set var="rowcolor" value="red"/>
        </c:if>
        <tr class="${rowcolor}">
            <td><fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }"/>
            </td>
            <td>${ meal.description }</td>
            <td>${ meal.calories }</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
