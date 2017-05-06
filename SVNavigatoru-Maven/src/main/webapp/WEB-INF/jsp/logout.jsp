<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>


<spring:eval expression="T(com.svnavigatoru600.service.util.UserUtils).isLogged()" var="isLogged" />
<c:if test="${isLogged}">
	<div class="right">
		<%-- The user is logged in; hence shows his full name and the log-out button. --%>

		<spring:eval expression="T(com.svnavigatoru600.service.util.UserUtils).loggedUser.getFullName()"
			var="loggedUserFullName" />
		<c:out value="${loggedUserFullName}" />

		<spring:eval expression="T(com.svnavigatoru600.web.url.users.UserAccountUrlParts).BASE_URL" var="userAccountUrl" />
		<br /> <a href="<c:url value="${userAccountUrl}" />"><spring:message code="user-account.title" /></a> | <a
			href="<c:url value="/j_spring_security_logout" />"><spring:message code="login.log-out" /></a>
	</div>
</c:if>
