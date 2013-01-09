<%-- For more details about the concepts used here, see the /users/administration/authority-administration.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.web.forum.contributions.AbstractNewEditContributionController"%>

<c:set var="baseUrl" value="/forum/temata/existujici/${newEditContributionCommand.threadId}/prispevky/" />
<a href="<c:url value="${baseUrl}" />">&laquo; <spring:message
		code="forum.contributions.go-back-to-list-of-contributions" />
</a>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditContributionCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="threadId" />
	<form:hidden path="contribution.id" />
	<form:hidden path="contribution.creationTime" />

	<table>
		<tr>
			<td><form:label path="contribution.text">
					<spring:message code="forum.contributions.text" />:</form:label>
			</td>
			<td><form:textarea path="contribution.text" class="notStandaloneWysiwygEditor" cols="20" rows="10" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td id="sendNotification">
				<c:set var="sendNotification" value="${newEditContributionCommand.sendNotification}" />
				<%@ include file="/WEB-INF/jsp/main-content/notification-checkbox.jsp"%>
			</td>
		</tr>


		<tr>
			<td></td>
			<td><c:set var="successUrl" value="${baseUrl}${successUrlEnd}" /> <input type="submit"
				value="<spring:message code="${buttonTittle}" />"
				onclick="setFormAction('<%=AbstractNewEditContributionController.COMMAND%>', '<c:url value='${successUrl}' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var contributionTextErrors = '<form:errors path="contribution.text" />';
	if (contributionTextErrors != "") {
		$("#contribution\\.text").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#contribution\\.text").focus();
//-->
</script>
