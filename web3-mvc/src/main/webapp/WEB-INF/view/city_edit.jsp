<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="BASE" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>城市管理 - 编辑城市</title>
</head>
<body>

<h1>编辑城市界面</h1>

<form id="city_form">
    <input type="hidden" name="id" value="${city.id}">
    <table>
        <tr>
            <td>城市名称：</td>
            <td>
                <input type="text" name="name" value="${city.name}">
            </td>
        </tr>
        <tr>
            <td>国家：</td>
            <td>
                <input type="text" name="country" value="${city.country}">
            </td>
        </tr>
        <tr>
            <td>省：</td>
            <td>
                <input type="text" name="state" value="${city.state}">
            </td>
        </tr>
    </table>
    <button type="submit">保存</button>
</form>

<script src="${BASE}/asset/lib/jquery/jquery.min.js"></script>
<script src="${BASE}/asset/lib/jquery-form/jquery.form.min.js"></script>
<script>
    $(function() {
        $('#city_form').ajaxForm({
            type: 'put',
            url: '${BASE}/city_edit',
            contentType: 'application/x-www-form-urlencoded; charset=utf-8',
            success: function(data) {
                if (data) {
                    location.href = '${BASE}/city';
                }
            }
        });
    });
</script>

</body>
</html>