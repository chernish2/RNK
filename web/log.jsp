<%@ page import="ru.workmap.util.LoggerUtil" %>
<%--
  Created by IntelliJ IDEA.
  User: chernish2
  Date: 23.03.12
  Time: 10:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
Error log<br>
<%=LoggerUtil.getErrorLog()%>
</body>
</html>