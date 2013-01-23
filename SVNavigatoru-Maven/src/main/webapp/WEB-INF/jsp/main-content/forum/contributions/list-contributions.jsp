<%-- For more details about the concepts used here, see the /users/administration/list-users.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.forum.contributions.ShowAllContributions"%>
<%@ page import="com.svnavigatoru600.web.forum.contributions.ListContributionsController"%>

<c:if test="${showAllContributionsCommand.contributionCreated}">
	<p class="successfulOperation">
		<spring:message code="forum.contributions.contribution-has-been-successfully-added" />
	</p>
</c:if>
<c:if test="${showAllContributionsCommand.contributionDeleted}">
	<p class="successfulOperation">
		<spring:message code="forum.contributions.contribution-has-been-successfully-deleted" />
	</p>
</c:if>
<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${error}" />
	</p>
</c:if>


<strong><a href="<c:url value="/forum/temata/novy/" />"><spring:message code="forum.threads.add-new-thread" />
</a>
</strong>

<h5>
	<spring:message code="forum.threads.thread" />
	: <em><c:out value="${showAllContributionsCommand.thread.name}" />
	</em>
</h5>
<c:set var="sectionUrl" value="forum/temata/existujici/${showAllContributionsCommand.thread.id}/prispevky" />

<%
// Gets the command from the ModelMap.
ShowAllContributions command = (ShowAllContributions) request.getAttribute(ListContributionsController.COMMAND);
List<Contribution> contributions = command.getContributions();
if (contributions.size() > 0) {
%>
<ul id="comments">
	<%}

User loggedUser = UserUtils.getLoggedUser();
String homeUrl = request.getContextPath();
Locale locale = Localization.getLocale(request);
for (Contribution contribution : contributions) {
	int contributionId = contribution.getId();
%>
	<li class="comment" id="contribution_<%=contributionId%>">
		<div class="comment-mask">
			<div class="comment-main">
				<div class="comment-wrap1">
					<div class="comment-wrap2">
						<div class="comment-head">
							<p>
								<% User contributionAuthor = contribution.getAuthor(); %>
								<strong><%=contributionAuthor.getFullName()%></strong>,
								<%
									Date creationTime = contribution.getCreationTime();
									Date lastSaveTime = contribution.getLastSaveTime();
									%>
								<small> <spring:message code="edit.added" />: <% String formattedDate = DateUtils.format(creationTime, DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale); %>
									<%=formattedDate%>
									<%
										if (!creationTime.equals(lastSaveTime)) {
											%>, <spring:message code="edit.updated" />: <% formattedDate = DateUtils.format(lastSaveTime, DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale); %>
									<%=formattedDate%> <%}%> </small>
							</p>


							<%-- Administration of contributions --%>
							<%
								boolean loggedUserIsAuthor = (contributionAuthor != null) && loggedUser.getUsername().equals(contributionAuthor.getUsername());
								if (loggedUserIsAuthor) {
								%>
							<p class="controls">
								<%-- Edit icon --%>
								<a href="<%=homeUrl%>/<c:out value="${sectionUrl}" />/existujici/<%=contributionId%>/"
									title="<spring:message code="forum.contributions.modify-contribution" />" class="edit"></a>

								<%-- Delete icon --%>
								<a id="delete[<%=contributionId%>]" href="#"
									title="<spring:message code="forum.contributions.delete-contribution" />" class="delete"
									onclick="if (confirm('<%=command.getLocalizedDeleteQuestions().get(contribution)%>')) {
													document.getElementById('delete[<%=contributionId%>]').setAttribute('href', '<%=homeUrl
														%>/<c:out value="${sectionUrl}" />/existujici/<%=contributionId%>/smazat/'); }"></a>
							</p>
							<%}%>
						</div>
						<div class="comment-body clearfix">
							<p><%=contribution.getText()%></p>
						</div>
					</div>
				</div>
			</div>
		</div></li>
	<%}

if (contributions.size() > 0) {
%>
</ul>
<%}%>


<strong><a href="<c:url value="/${sectionUrl}/novy/" />"><spring:message
			code="forum.contributions.add-new-contribution" />
</a>
</strong>
