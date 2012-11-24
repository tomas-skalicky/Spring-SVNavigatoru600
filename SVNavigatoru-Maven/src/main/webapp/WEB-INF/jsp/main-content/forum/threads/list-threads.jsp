<%-- For more details about the concepts used here, see the /users/administration/list-users.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.service.forum.threads.ShowAllThreads"%>
<%@ page import="com.svnavigatoru600.web.forum.threads.ListThreadsController"%>

<c:if test="${showAllThreadsCommand.threadCreated}">
	<p class="successfulOperation">
		<spring:message code="forum.threads.thread-has-been-successfully-added" />
	</p>
</c:if>
<c:if test="${showAllThreadsCommand.threadDeleted}">
	<p class="successfulOperation">
		<spring:message code="forum.threads.thread-has-been-successfully-deleted" />
	</p>
</c:if>
<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${error}" />
	</p>
</c:if>


<c:set var="sectionUrl" value="forum/temata" />
<p>
	<strong><a href="<c:url value="/${sectionUrl}/novy/" />"><spring:message
				code="forum.threads.add-new-thread" />
	</a>
	</strong>
</p>

<%
// Gets the command from the ModelMap.
ShowAllThreads command = (ShowAllThreads) request.getAttribute(ListThreadsController.COMMAND);
List<Thread> threads = command.getThreads();
if (threads.size() > 0) {
%>
<table>
	<tr>
		<th><spring:message code="forum.threads.name" />
		</th>
		<th><spring:message code="forum.threads.last-change" />
		</th>
		<th></th>
		<th></th>
	</tr>
	<%}

User loggedUser = UserUtils.getLoggedUser();
String homeUrl = request.getContextPath();
Locale locale = Localization.getLocale(request);
for (Thread thread : threads) {
	int threadId = thread.getId();
%>
	<tr>
		<td><a href="<%=homeUrl%>/<c:out value="${sectionUrl}" />/existujici/<%=threadId
				%>/prispevky/"><%=thread.getName()%></a>
		</td>
		<td>
			<%
				Contribution contribution = command.getLastSavedContributions().get(thread);
				if (contribution != null) {
					User contributionAuthor = contribution.getAuthor();
					if (contributionAuthor != null) {
					%> <small><em><%=contributionAuthor.getFullName()%></em>
		</small><br /> <%}%> <% String formattedDate = DateUtils.format(contribution.getLastSaveTime(), DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale); %>
			<small><em><%=formattedDate%></em>
		</small> <%}%>
		</td>


		<%-- Administration of threads --%>
		<%
			User threadAuthor = thread.getAuthor();
			boolean loggedUserIsAuthor = (threadAuthor != null) && loggedUser.getUsername().equals(threadAuthor.getUsername());
			if (loggedUserIsAuthor) {
			%>
		<%-- Edit icon --%>
		<td><a href="<%=homeUrl%>/<c:out value="${sectionUrl}" />/existujici/<%=threadId%>/"
			title="<spring:message code="forum.threads.modify-thread" />" class="edit"></a>
		</td>

		<%-- Delete icon --%>
		<td><a id="delete[<%=threadId%>]" href="#" title="<spring:message code="forum.threads.delete-thread" />"
			class="delete"
			onclick="if (confirm('<%=command.getLocalizedDeleteQuestions().get(thread)%>')) {
							document.getElementById('delete[<%=threadId%>]').setAttribute('href', '<%=homeUrl
								%>/<c:out value="${sectionUrl}" />/existujici/<%=threadId%>/smazat/'); }"></a>
		</td>
		<%} else {%>
		<td></td>
		<td></td>
		<%}%>
	</tr>
	<%}

if (threads.size() > 0) {
%>
</table>
<%}%>
