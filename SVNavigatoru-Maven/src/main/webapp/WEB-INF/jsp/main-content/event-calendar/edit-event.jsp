<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<c:if test="${newEditEventCommand.dataSaved}">
	<p class="successfulOperation">
		<spring:message code="edit.changes-successfully-saved" />
	</p>
</c:if>

<c:set var="buttonTittle" value="edit.save-changes" />
<c:set var="successUrlEnd" value="existujici/${newEditEventCommand.event.id}/" />
<%@ include file="/WEB-INF/jsp/main-content/event-calendar/new-edit-event.jsp"%>
