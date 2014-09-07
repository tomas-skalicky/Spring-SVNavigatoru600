<%-- For more details about the concepts used here, see the records/other-documents/templates/list-records.jsp file. --%>

<%-- Statically included (= copied) to /records/session/all/list-records.jsp and to some other JSP pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.records.session.ShowAllSessionRecords"%>
<%@ page import="com.svnavigatoru600.web.records.session.AbstractListRecordsController"%>

<c:if test="${showAllRecordsCommand.recordCreated}">
	<p class="successfulOperation">
		<spring:message code="session-records.record-has-been-successfully-added" />
	</p>
</c:if>
<c:if test="${showAllRecordsCommand.recordDeleted}">
	<p class="successfulOperation">
		<spring:message code="session-records.record-has-been-successfully-deleted" />
	</p>
</c:if>
<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${error}" />
	</p>
</c:if>

<my:loggedUser checkIfCanEditSessionRecords="true">
	<p>
		<strong><a href="<c:url value="/${sectionUrl}/novy/" />"><spring:message
					code="session-records.add-new-record" /></a></strong>
	</p>
	
	<h5><spring:message code="session-records.existing-records" /></h5>
</my:loggedUser>



<%
// Gets the command from the ModelMap.
ShowAllSessionRecords command = (ShowAllSessionRecords) request.getAttribute(AbstractListRecordsController.COMMAND);
List<SessionRecord> records = command.getRecords();
if (records.size() > 0) {
%>
<table>
	<tr>
		<%if (command.isAllRecordTypes()) {%>
		<th><spring:message code="session-records.type-of-session" />
		</th>
		<%}%>
		<th><spring:message code="session-records.session-date" />
		</th>
		<th><spring:message code="session-records.discussed-topics" />
		</th>
		<th></th>

		<%-- Administration of records --%>
		<my:loggedUser checkIfCanEditSessionRecords="true">
			<th></th>
			<th></th>
		</my:loggedUser>
	</tr>
	<%}

String homeUrl = request.getContextPath();
String fileStorageUrl = String.format("%s/%s", homeUrl, Configuration.FILE_STORAGE);
for (SessionRecord record : records) {
	int recordId = record.getId();
%>
	<tr>
		<%if (command.isAllRecordTypes()) {%>
		<td><%=command.getLocalizedTypeTitles().get(record)%></td>
		<%}%>
		<td><em><%=command.getLocalizedSessionDates().get(record)%></em>
		</td>
		<td><%=record.getDiscussedTopics()%></td>

		<%-- Retrieve the attached file --%>
		<td>
			<%-- The file is stored in the file system, not in the DB. --%> <%-- 			<a href="<%=fileStorageUrl%><%=record.getFileName()%>" --%>
			<%-- 				title="<spring:message code="session-records.download-record" />" class="save"></a> --%> <%-- The file is stored in the DB. --%>
			<a id="file[<%=recordId%>]" href="#" title="<spring:message code="session-records.download-record" />" class="save"
			onclick="document.getElementById('file[<%=recordId%>]').setAttribute('href', '<%=homeUrl
								%>/<c:out value="${sectionUrl}" />/existujici/<%=recordId%>/stahnout/');"></a>
		</td>

		<%-- Administration of records --%>
		<my:loggedUser checkIfCanEditSessionRecords="true">
			<%-- Edit icon --%>
			<td><a href="<%=homeUrl%>/<c:out value="${sectionUrl}" />/existujici/<%=recordId%>/"
				title="<spring:message code="session-records.modify-record" />" class="edit"></a>
			</td>
	
			<%-- Delete icon --%>
			<td><a id="delete[<%=recordId%>]" href="#" title="<spring:message code="session-records.delete-record" />"
				class="delete"
				onclick="if (confirm('<%=command.getLocalizedDeleteQuestions().get(record)%>')) {
								document.getElementById('delete[<%=recordId%>]').setAttribute('href', '<%=homeUrl
									%>/<c:out value="${sectionUrl}" />/existujici/<%=recordId%>/smazat/'); }"></a>
			</td>
		</my:loggedUser>
	</tr>
	<%}

if (records.size() > 0) {
%>
</table>
<%}%>
