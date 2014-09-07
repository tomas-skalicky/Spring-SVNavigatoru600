<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<%@page import="com.svnavigatoru600.url.ErrorsUrlParts"%>
<%@page import="com.svnavigatoru600.url.news.NewsUrlParts"%>
<%@page import="com.svnavigatoru600.url.users.UserAccountUrlParts"%>
<%@page import="com.svnavigatoru600.url.users.UserAdministrationUrlParts"%>


<c:set var="homepage" value="<%=ErrorsUrlParts.ERROR_403%>" />

<my:currentUser checkIfLogged="true">
	<%-- If the user fulfills more of these conditions, the last one wins. --%>
	
	<my:loggedUser checkIfCanSeeHisAccount="true">
		<c:set var="homepage" value="<%=UserAccountUrlParts.BASE_URL%>" />
	</my:loggedUser>
	<my:loggedUser checkIfCanSeeUserAdministration="true">
		<c:set var="homepage" value="<%=UserAdministrationUrlParts.BASE_URL%>" />
	</my:loggedUser>
	<my:loggedUser checkIfCanSeeNews="true">
		<c:set var="homepage" value="<%=NewsUrlParts.BASE_URL%>" />
	</my:loggedUser>
	
</my:currentUser>
<my:currentUser checkIfNotLogged="true">
	<c:set var="homepage" value="/" />
</my:currentUser>

<a href="<c:url value="${homepage}" />"><spring:message code="application.title" /> </a>
