<%@ page pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="BASE" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>客户管理 - 查看客户</title>
</head>
<body>

<h1>查看客户界面</h1>

<%-- TODO --%>
<table>
    <tr>
        <th>城市序号</th>
        <th>城市名称</th>
        <th>国家</th>
        <th>省</th>
    </tr>
    <tr>
        <td>${city.id}</td>
        <td>${city.name}</td>
        <td>${city.country}</td>
        <td>${city.state}</td>
    </tr>
</table>

</body>
</html>