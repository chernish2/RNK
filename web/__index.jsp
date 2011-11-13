<%--
  Created by IntelliJ IDEA.
  User: chernish2
  Date: 15.10.11
  Time: 22:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="bean.MyBean" %>
<jsp:useBean id="mybean" class="bean.MyBean"></jsp:useBean>
<jsp:setProperty name="mybean" property="*"></jsp:setProperty>
<html>
<head><title>Simple jsp page</title></head>
<body>
Enter city:
<form method="post" action="__index.jsp">
    <input type="text" id="region" name="region">
    <input type="submit" value="submit">
</form>
<%= mybean.search()

%>

</body>
</html>