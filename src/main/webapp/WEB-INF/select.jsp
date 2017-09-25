<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Package Selection!</title>
</head>
<body style="font-family:arial">
	<form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout!" />
    </form><br>
	<h1>Welcome to Dojoscriptions <c:out value ="${currentUser.firstName}"/>!</h1>
	<p>Please choose a subscription and a start date.</p>
	<form action="/users/${currentUser.id}" method="POST">
		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
		
		Due Date:
		<select name="due">
			<c:forEach begin="1" end="31" varStatus="loop">
				<option value="${loop.index}">${loop.index}</option>
			</c:forEach>
		</select><br>
		Package:
		<select name="pack">
			<c:forEach items="${packages}" var="p">
				<c:if test="${p.available}">
				<option value="${p.id}">${p.name} ($${p.price}0)</option>
				</c:if>
			</c:forEach>
		</select><br>
		<input type="submit" value="Sign up!">
	</form>
</body>
</html>