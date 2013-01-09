<%-- For more details about the concepts used here, see the /users/administration/authority-administration.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.web.forum.threads.AbstractNewEditThreadController"%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditThreadCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />

	<table>
		<tr>
			<td><form:label path="thread.name">
					<spring:message code="forum.threads.name" />:</form:label>
			</td>
			<td><form:input path="thread.name" autocomplete="${newEditThreadCommand.thread.name}" class="longText" />
			</td>
		</tr>
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
				<c:set var="sendNotification" value="${newEditThreadCommand.sendNotification}" />
				<%@ include file="/WEB-INF/jsp/main-content/notification-checkbox.jsp"%>
			</td>
		</tr>


		<tr>
			<td></td>
			<td><c:set var="buttonTittle" value="forum.threads.add-thread" /> <c:set var="successUrl"
					value="/forum/temata/novy/" /> <input type="submit" value="<spring:message code="${buttonTittle}" />"
				onclick="setFormAction('<%=AbstractNewEditThreadController.COMMAND%>', '<c:url value='${successUrl}' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var threadNameErrors = '<form:errors path="thread.name" />';
	if (threadNameErrors != "") {
		$("#thread\\.name").addClass("errorInput");
	}
	var contributionTextErrors = '<form:errors path="contribution.text" />';
	if (contributionTextErrors != "") {
		$("#contribution\\.text").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#thread\\.name").focus();
//-->
</script>
