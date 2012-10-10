<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<%-- fileChanged should be false at the beginning. See service.records.otherdocuments.EditRecord.java. --%>
<form:hidden path="fileChanged" />
<form:hidden path="record.fileName" />

<span id="record.file.existing"> <c:out value="${newEditRecordCommand.record.fileName}" /> <a id="deleteFile"
	href="#" title="<spring:message code="edit.change" />" class="delete"
	onclick="if (confirm('<spring:message code="other-documents.do-you-really-want-to-delete-document"
											arguments="${newEditRecordCommand.record.name}" />')) {
		$('#fileChanged').val(true);
		$('#record\\.file\\.existing').addClass('invisible');
		$('#record\\.file\\.new').removeClass('invisible'); }">
</a> </span>
<span id="record.file.new" class="invisible"> <%@ include file="/WEB-INF/jsp/main-content/records/new-file.jsp"%>
</span>
