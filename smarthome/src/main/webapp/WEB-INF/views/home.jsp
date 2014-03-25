<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
    <head>
        <title>Welcome to raspsonar</title>
    </head>
    <body>    
        <h2>Actual water level: <c:out value="${waterLevel}"/></h2>
        <h2>Configuration folder: <c:out value="${configurationFolder}"/></h2>
        <a href="<c:url value="/editConfiguration"/>">Configuration</a>
    </body>
</html>

