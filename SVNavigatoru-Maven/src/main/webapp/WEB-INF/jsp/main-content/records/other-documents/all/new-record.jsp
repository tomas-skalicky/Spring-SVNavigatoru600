<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<spring:eval expression="T(com.svnavigatoru600.web.url.records.otherdocuments.AllDocumentsUrlParts).NEW_URL" var="newRecordUrl" />
<c:set var="successUrl" value="${newRecordUrl}" />
<%@ include file="/WEB-INF/jsp/main-content/records/other-documents/templates/new-record.jsp"%>
