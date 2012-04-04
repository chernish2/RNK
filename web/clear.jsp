<%@ page import="ru.workmap.cache.DBCache" %>
<%--
  Created by IntelliJ IDEA.
  User: chernish2
  Date: 18.03.12
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Simple jsp page</title></head>
<body>
<%DBCache.getInstance().clear(); %>
DB cache is cleared
</body>
</html>