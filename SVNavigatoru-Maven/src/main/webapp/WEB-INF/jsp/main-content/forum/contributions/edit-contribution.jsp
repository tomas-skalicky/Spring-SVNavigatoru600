<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<c:if test="${newEditContributionCommand.dataSaved}">
	<p class="successfulOperation">
		<spring:message code="edit.changes-successfully-saved" />
	</p>
</c:if>

<c:set var="buttonTittle" value="edit.save-changes" />
<c:set var="successUrlEnd" value="existujici/${newEditContributionCommand.contribution.id}/" />
<%@ include file="/WEB-INF/jsp/main-content/forum/contributions/new-edit-contribution.jsp"%>
