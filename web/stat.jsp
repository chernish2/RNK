<%@ page import="ru.workmap.util.Statistics" %>
<%@ page import="ru.workmap.cache.DBCache" %>
<%@ page import="ru.workmap.HHSearcher" %>
<%--
  Created by IntelliJ IDEA.
  User: a.chernysh
  Date: 31.01.12
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>workmap.ru statistics</title>
</head>
<body>
STATISTICS<br><br>
Totals hits: <%=Statistics.getHits() %><br>
Total searches: <%=Statistics.getSearches()%><br>
<br>
Memory: <%=Runtime.getRuntime().freeMemory() / 1024%> kb free out of <%=Runtime.getRuntime().totalMemory() / 1024%> kb
<br><br>
Vacancy statistics:<br>
<%=HHSearcher.getStat().toString()%>



</body>
</html>