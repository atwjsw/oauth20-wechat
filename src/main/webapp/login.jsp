<%--
  Created by IntelliJ IDEA.
  User: wenda
  Date: 6/15/2017
  Time: 2:06 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/callBack" method="post">
    <input type="text" name="account">
    <input type="password" name="password">
    <input type="hidden" name="openid" value="${openid}">
    <input type="submit" value="登录并绑定">
</form>
</body>
</html>