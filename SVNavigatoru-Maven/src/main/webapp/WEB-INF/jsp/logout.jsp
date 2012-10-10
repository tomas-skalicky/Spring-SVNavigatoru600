<%@ page pageEncoding="UTF-8"%>
<%@ include file="include-preceding-html.jsp"%>

<% if (UserUtils.isLogged()) { %>
<div class="right">
	<%
			// The user is logged in; hence shows his full name and the log-out button.
			out.print(UserUtils.getLoggedUser().getFullName());
		%>
	<br /> <a href="<c:url value="/uzivatelsky-ucet/" />"><spring:message code="user-account.title" />
	</a> | <a href="<c:url value="/j_spring_security_logout" />"><spring:message code="login.log-out" />
	</a>
</div>
<% } %>
