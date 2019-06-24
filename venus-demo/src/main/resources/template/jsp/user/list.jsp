<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="demo.user.model.User" %>
<%@ page import="java.util.ArrayList" %>

<%
    Object result = request.getAttribute("result_list");
    List<User> users = new ArrayList();
    if (result!=null && result instanceof List) {
        users = (List) result;
    }
%>



<html>
<head>
    <title>User List</title>
</head>
<body>

    <%
        for (User user : users) {

    %>

        <%=user.getEmail() %>
        <br/>

    <%
        }
    %>


</body>
</html>
