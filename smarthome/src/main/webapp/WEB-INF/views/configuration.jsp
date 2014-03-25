<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<title>Configuration</title>
</head>
<body>
	<h2>Configuration</h2>
	<form:form modelAttribute="configuration">
		<c:if test="${not empty infoMessage}">
			<div style="color: green">${infoMessage}</div>
		</c:if>
		<c:if test="${not empty errorMessage}">
			<div style="color: red">${errorMessage}</div>
		</c:if>
		<table>
			<tr>
				<td>Email address:</td>
				<td><form:input size="40" path="email" /></td>				
				<td><input type="submit" value="Test" formmethod="get" formaction="${pageContext.request.contextPath}/configuration/mailTest"/></td>				
				<td><form:errors path="email" cssStyle="color: red" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Save configuration" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>