<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<c:if test="${newEditRecordCommand.dataSaved}">
	<p class="successfulOperation">
		<spring:message code="edit.changes-successfully-saved" />
	</p>
</c:if>

<c:set var="buttonTittle" value="edit.save-changes" />
<%@ include file="/WEB-INF/jsp/main-content/records/session/templates/new-edit-record.jsp"%>
