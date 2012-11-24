<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.service.users.ShowAllUsers"%>
<%@ page import="com.svnavigatoru600.service.util.FullNameFormat"%>

<%-- For more details about the concepts used here, see the forgotten-password.jsp file. --%>

<c:if test="${showAllUsersCommand.userCreated}">
	<p class="successfulOperation">
		<spring:message code="user-administration.user-has-been-successfully-created" />
	</p>
</c:if>
<c:if test="${showAllUsersCommand.userDeleted}">
	<p class="successfulOperation">
		<spring:message code="user-administration.user-has-been-successfully-deleted" />
	</p>
</c:if>
<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${error}" />
	</p>
</c:if>

<p>
	<strong><a href="<c:url value="/administrace-uzivatelu/novy/" />"><spring:message
				code="user-administration.add-new-user" />
	</a>
	</strong>
</p>

<%-- Prints all users in the application. All of them except the currently logged-in user can be modified,
i.e. there are necessary links. --%>
<h5>
	<spring:message code="user-administration.existing-users" />
</h5>



<%
// Gets the command from the ModelMap.
ShowAllUsers command = (ShowAllUsers) request.getAttribute("showAllUsersCommand");
List<User> users = command.getUsers();
if (users.size() > 0) {
%>
<ul>
	<%}

String homeUrl = request.getContextPath();
User loggedUser = UserUtils.getLoggedUser();
for (User user : users) {
%>
	<li>
		<%
			String username = user.getUsername();
			boolean isLoggedUser = username.equals(loggedUser.getUsername());
			if (!isLoggedUser) {%> <a href="<%=homeUrl%>/administrace-uzivatelu/existujici/<%=username%>/"
		title="<spring:message code="user-administration.change-user-data" />"> <%}
			out.print(user.getFullName(FullNameFormat.LAST_FIRST));
			if (!isLoggedUser) {
				%>
	</a> <%-- Delete icon --%> <%-- Disabled since the forum contributions are bound to particular users. --%> <%--<a id="delete[<%=username%>]" href="#" title="<spring:message code="user-administration.delete-user" />" class="delete"
						onclick="if (confirm('<%=command.getLocalizedDeleteQuestions().get(user)%>')) {
						document.getElementById('delete[<%=username%>]').setAttribute('href', '<%=homeUrl
								%>/administrace-uzivatelu/existujici/<%=username%>/smazat/'); }"></a>--%> <%} else {%> (<spring:message
			code="user.your-account" />) <%}%>
	</li>
	<%}

if (users.size() > 0) {
%>
</ul>
<%}%>
