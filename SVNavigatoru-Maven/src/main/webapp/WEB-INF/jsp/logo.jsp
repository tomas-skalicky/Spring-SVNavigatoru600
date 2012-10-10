<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<%
// Sets the homepage according to the fact whether the user is logged in and according to his authorities.
String homepage;

if (UserUtils.isLogged()) {
	User user = UserUtils.getLoggedUser();

	if (user.canSeeNews()) {
		homepage = "/novinky/";
	} else if (user.canSeeUsers()) {
		homepage = "/administrace-uzivatelu/";
	} else if (user.canSeeHisAccount()) {
		homepage = "/uzivatelsky-ucet/";
	} else {
		homepage = "/chyby/403/";
	}
} else {
	homepage = "/";
}
%>

<a href="<c:url value="<%=homepage%>" />"><spring:message code="application.title" /> </a>
