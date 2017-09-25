<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Admin Dashboard</title>
</head>
<body style="font-family:arial">
	<form id="logoutForm" method="POST" action="/logout">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="submit" value="Logout!" />
    </form>
    <h1>Customers</h1>
    <table style="width: 650px" border="1">
    	<tr>
    		<th>Name</th>
    		<th>Payment Due Date</th>
    		<th>Amount Due</th>
    		<th>Package Type</th>
    	</tr>
    	<c:forEach items="${users}" var="user">
    	<c:if test="${!user.checkIfAdmin()}">
    		<tr>
    		<td><c:out value="${user.firstName} ${user.lastName}"/></td>
    		<td><fmt:formatDate pattern="EEEEE, MMM d, yyyy" value="${user.dueDate}"/></td>
    		<td>$<c:out value="${user.pack.price}"/></td>
    		<td><c:out value="${user.pack.name}"/></td>
    	</tr>
    	</c:if>
    	</c:forEach>
    </table><br>
    
    <h1>Packages</h1>
    <table style="width: 550px" border="1">
    	<tr>
    		<th>Package Name</th>
    		<th>Package Cost</th>
    		<th>Availability</th>
    		<th>Users</th>
    		<th>Actions</th>
    	</tr>
    	<c:forEach items="${packages}" var="p">
    	<tr>
    		<td><c:out value="${p.name}"/></td>
    		<td><fmt:formatNumber value = "${p.price}" type = "currency"/></td>
    		<c:choose>
    			<c:when test="${p.available}">
    				<td>Available</td>
    			</c:when>
    			<c:otherwise><td>Unavailable</td></c:otherwise>
    		</c:choose>
    		<td><c:out value="${p.getUsers().size()}"/></td>
    		<td>
	    		<a href="/packages/changeAvailabilty/${p.id}">
	    		<button>
	    			<c:choose>
	    				<c:when test="${p.isAvailable()}">Deactivate</c:when>
	    				<c:otherwise>Activate</c:otherwise>
	    			</c:choose>
	    		</button>
	    		</a>
	    		<c:if test="${p.getUsers().size() < 1}">
	    		<a href="/packages/delPack/${p.id}"><button>Delete</button></a>
	    		</c:if>
    		</td>
    	</tr>
    	</c:forEach>
    </table><br>
    
    <fieldset style="width:400px">
    <legend>Create Package</legend>
    	<form:form action="/packages/new" method="post" modelAttribute="pack">
    		<form:input type="text" path="name" placeholder="Package Name"/><br>
    		<form:label path="price">Cost: <br>
    			<form:input type="number" path="price" min="0.00" value="0.00" step="0.01"/><br>
    		</form:label>
    		<input type="submit" value="Create">
    	</form:form>
    	<form:errors path="pack.*" />
    </fieldset>
</body>
</html>