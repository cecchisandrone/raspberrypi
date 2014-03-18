<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Configuration</title>
</head>
<body>
<h2>Configuration</h2>
<form:form method="post" modelAttribute="configuration" action="/saveConfiguration">
 
    <table>
    <tr>
        <td>Email</td>
        <td><form:input path="email" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Save configuration"/>
        </td>
    </tr>
</table>  
     
</form:form>
</body>
</html>