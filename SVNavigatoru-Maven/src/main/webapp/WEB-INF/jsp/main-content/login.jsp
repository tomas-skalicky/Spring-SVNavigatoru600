<%@ page pageEncoding="UTF-8"%>
<%@ include file="../include-preceding-html.jsp"%>
<%@ page import="org.apache.commons.lang3.StringEscapeUtils"%>

<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
	</p>
</c:if>

<form name="login_form" action="<c:url value="/j_spring_security_check"/>" method="POST">
	<table>
		<tr>
			<td><label for="login"><spring:message code="username" /> / <spring:message code="email" />:</label>
			</td>
			<%
				// Necessary since the username is escaped.
				String username = "";
				Object usernameObj = session.getAttribute("SPRING_SECURITY_LAST_USERNAME");
				if (usernameObj != null) {
					username = StringEscapeUtils.unescapeHtml4(usernameObj.toString());
				}
			%>
			<td><input id="login" type="text" name="j_username" class="longText"
				value="<c:if test="${not empty error}"><c:out value="<%=username%>"/></c:if>" /></td>
		</tr>
		<tr>
			<td><label for="password"><spring:message code="password" />:</label></td>
			<td><input id="password" type="password" name="j_password" class="longText" /></td>
		</tr>
		<tr>
			<td><input id="rememberMe" type="checkbox" name="_spring_security_remember_me" /> <label for="rememberMe"><spring:message
						code="login.remember-me" /> </label>
			</td>
			<td><input type="submit" value="<spring:message code="login.log-in" />" /></td>
		</tr>
	</table>
</form>
<a href="<c:url value="/prihlaseni/zapomenute-heslo/" />"><spring:message code="login.forgotten-password" /></a>



<%-- Helps to focus the username field during the loading of the page. --%>
<script type="text/javascript">
<!--
	// jQuery
	$("#login").focus();
//-->
</script>
