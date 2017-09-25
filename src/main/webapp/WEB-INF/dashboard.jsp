<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dashboard</title>
</head>
<body style="font-family:arial">
	<h1>Welcome <c:out value ="${currentUser.firstName}"/>!</h1>
	<fieldset style="width: 550px">
	<legend><b>Customer: <c:out value ="${currentUser.firstName} ${currentUser.lastName}"/> package information</b></legend>
	Current Package: <b><c:out value="${currentUser.pack.name}"/></b><br>
	Next Due Date: <b><fmt:formatDate pattern="EEEEE, MMM d, yyyy" value="${currentUser.dueDate}"/></b><br>
	User Since: <b><fmt:formatDate pattern="EEEEE, MMM d, yyyy" value="${currentUser.createdAt}"/></b><br>
	<%--<fmt:formatDate type="time" timeStyle = "short" value="${currentUser.createdAt}"/></b><br>--%>
	Last Login: <b><fmt:formatDate pattern="EEEEE, MMM d, yyyy" value="${currentUser.updatedAt}"/>
	<fmt:formatDate type="time" timeStyle = "short" value="${currentUser.updatedAt}"/></b><br>
	</fieldset><br>
	
	<form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout!" />
    </form><br>
</body>
</html>