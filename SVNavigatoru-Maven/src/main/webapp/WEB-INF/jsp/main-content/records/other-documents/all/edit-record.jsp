<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<spring:eval expression="T(com.svnavigatoru600.url.records.otherdocuments.AllDocumentsUrlParts).EXISTING_URL" var="existingRecordUrl" />
<c:set var="successUrl" value="${existingRecordUrl}${newEditRecordCommand.record.id}/" />
<%@ include file="/WEB-INF/jsp/main-content/records/other-documents/templates/edit-record.jsp"%>
