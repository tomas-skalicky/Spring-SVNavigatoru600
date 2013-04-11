<%-- For more details about the concepts used here, see the users/administration/list-users.jsp file. --%>

<%-- Statically included (= copied) to /records/other-documents/accounting/list-records.jsp and to some other JSP pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.records.otherdocuments.ShowAllRecords"%>
<%@ page import="com.svnavigatoru600.web.records.otherdocuments.AbstractListDocumentsController"%>

<c:if test="${showAllRecordsCommand.recordCreated}">
	<p class="successfulOperation">
		<spring:message code="other-documents.document-has-been-successfully-added" />
	</p>
</c:if>
<c:if test="${showAllRecordsCommand.recordDeleted}">
	<p class="successfulOperation">
		<spring:message code="other-documents.document-has-been-successfully-deleted" />
	</p>
</c:if>
<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${error}" />
	</p>
</c:if>

<my:loggedUser checkIfCanEditOtherDocumentRecords="true">
	<p>
		<strong><a href="<c:url value="/${sectionUrl}/novy/" />"><spring:message
					code="other-documents.add-new-document" /></a></strong>
	</p>
	
	<h5><spring:message code="other-documents.existing-documents" /></h5>
</my:loggedUser>



<%
// Gets the command from the ModelMap.
ShowAllRecords command = (ShowAllRecords) request.getAttribute(AbstractListDocumentsController.COMMAND);
List<OtherDocumentRecord> records = command.getRecords();
if (records.size() > 0) {
%>
<table>
	<tr>
		<th><spring:message code="other-documents.document-name" />
		</th>
		<th><spring:message code="other-documents.document-description" />
		</th>
		<th></th>
		<th><spring:message code="edit.updated" />
		</th>

		<%-- Administration of records --%>
		<my:loggedUser checkIfCanEditOtherDocumentRecords="true">
			<th></th>
			<th></th>
		</my:loggedUser>
	</tr>
	<%}

String homeUrl = request.getContextPath();
String fileStorageUrl = String.format("%s/%s", homeUrl, Configuration.FILE_STORAGE);
Locale locale = Localization.getLocale(request);
for (OtherDocumentRecord record : records) {
	int recordId = record.getId();
%>
	<tr>
		<td><em><%=record.getName()%></em>
		</td>
		<td><small><%=record.getDescription()%></small>
		</td>

		<%-- Retrieve the attached file --%>
		<td>
			<%-- The file is stored in the file system, not in the DB. --%> <%-- 			<a href="<%=fileStorageUrl%><%=record.getFileName()%>" --%>
			<%-- 				title="<spring:message code="other-documents.download-document" />" class="save"></a> --%> <%-- The file is stored in the DB. --%>
			<a id="file[<%=recordId%>]" href="#" title="<spring:message code="other-documents.download-document" />" class="save"
			onclick="document.getElementById('file[<%=recordId%>]').setAttribute('href', '<%=homeUrl
								%>/<c:out value="${sectionUrl}" />/existujici/<%=recordId%>/stahnout/');"></a>
		</td>

		<% String formattedDate = DateUtils.format(record.getLastSaveTime(), DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale); %>
		<td><small><%=formattedDate%></small>
		</td>

		<%-- Administration of records --%>
		<my:loggedUser checkIfCanEditOtherDocumentRecords="true">
			<%-- Edit icon --%>
			<td><a href="<%=homeUrl%>/<c:out value="${sectionUrl}" />/existujici/<%=recordId%>/"
				title="<spring:message code="other-documents.modify-document" />" class="edit"></a>
			</td>
	
			<%-- Delete icon --%>
			<td><a id="delete[<%=recordId%>]" href="#" title="<spring:message code="other-documents.delete-document" />"
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
