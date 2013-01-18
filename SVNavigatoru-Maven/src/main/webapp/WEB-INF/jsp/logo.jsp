<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<%@page import="com.svnavigatoru600.url.ErrorsUrlParts"%>
<%@page import="com.svnavigatoru600.url.news.NewsUrlParts"%>
<%@page import="com.svnavigatoru600.url.users.UserAccountUrlParts"%>
<%@page import="com.svnavigatoru600.url.users.UserAdministrationUrlParts"%>


<%
    // Sets the homepage according to the fact whether the user is logged in and according to his authorities.
String homepage;

if (UserUtils.isLogged()) {
	User user = UserUtils.getLoggedUser();

	if (user.canSeeNews()) {
		homepage = NewsUrlParts.BASE_URL;
	} else if (user.canSeeUsers()) {
		homepage = UserAdministrationUrlParts.BASE_URL;
	} else if (user.canSeeHisAccount()) {
		homepage = UserAccountUrlParts.BASE_URL;
	} else {
		homepage = ErrorsUrlParts.ERROR_403;
	}
} else {
	homepage = "/";
}
%>

<a href="<c:url value="<%=homepage%>" />"><spring:message code="application.title" /> </a>
