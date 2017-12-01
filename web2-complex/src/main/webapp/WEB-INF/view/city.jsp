<%--
  Created by IntelliJ IDEA.
  User: xiao
  Date: 17/12/1
  Time: 下午4:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="BASE" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>城市管理</title>
</head>
<body>

<h1>城市列表</h1>

<table>
    <tr>
        <th>城市序号</th>
        <th>城市名称</th>
        <th>国家</th>
        <th>省</th>
        <th>操作</th>
    </tr>
    <c:forEach var="city" items="${cityList}">
        <tr>
            <td>${city.id}</td>
            <td>${city.name}</td>
            <td>${city.country}</td>
            <td>${city.state}</td>
            <td>
                <a href="${BASE}/city_edit?id=${city.id}">编辑</a>
                <a href="${BASE}/city_delete?id=${city.id}">删除</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
