<%-- For more details about the concepts used here, see the /users/administration/authority-administration.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.web.forum.threads.AbstractNewEditThreadController"%>

<c:if test="${newEditThreadCommand.dataSaved}">
	<p class="successfulOperation">
		<spring:message code="edit.changes-successfully-saved" />
	</p>
</c:if>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditThreadCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="thread.id" />
	<form:hidden path="thread.creationTime" />

	<table>
		<tr>
			<td><form:label path="thread.name">
					<spring:message code="forum.threads.name" />:</form:label>
			</td>
			<td><form:input path="thread.name" autocomplete="${newEditThreadCommand.thread.name}" class="longText" />
			</td>
		</tr>


		<tr>
			<td></td>
			<td><c:set var="buttonTittle" value="edit.save-changes" /> <c:set var="successUrl"
					value="/forum/temata/existujici/${newEditThreadCommand.thread.id}/" /> <input type="submit"
				value="<spring:message code="${buttonTittle}" />"
				onclick="setFormAction('<%=AbstractNewEditThreadController.COMMAND%>', '<c:url value='${successUrl}' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var threadNameErrors = '<form:errors path="thread.name" />';
	if (threadNameErrors != "") {
		$("#thread\\.name").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#thread\\.name").focus();
//-->
</script>
