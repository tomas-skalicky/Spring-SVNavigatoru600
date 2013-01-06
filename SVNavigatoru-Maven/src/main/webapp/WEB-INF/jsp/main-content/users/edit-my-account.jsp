<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<%-- For more details about the concepts used here, see the forgotten-password.jsp file. --%>

<c:if test="${updateUserDataCommand.dataSaved}">
	<p class="successfulOperation">
		<spring:message code="edit.changes-successfully-saved" />
	</p>
</c:if>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="updateUserDataCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />

	<table>
		<%-- Login data --%>
		<tr>
			<td><label for="username"><spring:message code="username" />:</label></td>
			<td>
				<%-- The tag <form:hidden> is necessary to submit the username
					since disabled tags <input> and <form:input> do not do that. --%> <c:out
					value="${updateUserDataCommand.user.username}" /> <form:hidden path="user.username" /></td>
		</tr>
		<tr>
			<td><form:label path="newPassword"><spring:message code="password.new" />:</form:label></td>
			<td><form:password path="newPassword" autocomplete="${updateUserDataCommand.newPassword}" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="newPasswordConfirmation"><spring:message code="password.confirm-new" />:</form:label></td>
			<td><form:password path="newPasswordConfirmation"
					autocomplete="${updateUserDataCommand.newPasswordConfirmation}" class="longText" /></td>
		</tr>
		<tr>
			<td colspan="2"><span class="formNote"><spring:message
						code="edit-user-data.your-password-will-not-change-if" /></span></td>
		</tr>


		<%-- Other user's data --%>
		<tr>
			<td><label for="fullName"><spring:message code="full-name" />:</label></td>
			<td><c:out value="${updateUserDataCommand.user.firstName} ${updateUserDataCommand.user.lastName}" /> <form:hidden
					path="user.firstName" /> <form:hidden path="user.lastName" /></td>
		</tr>
		<tr>
			<td><form:label path="user.email"><spring:message code="email" />:</form:label></td>
			<td><form:input path="user.email" autocomplete="${updateUserDataCommand.user.email}" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="user.phone"><spring:message code="phone" />:</form:label></td>
			<td><form:input path="user.phone" autocomplete="${updateUserDataCommand.user.phone}" class="longText" /></td>
		</tr>


		<tr>
			<td></td>
			<td><input type="submit" value="<spring:message code="edit.save-changes" />"
				onclick="setFormAction('updateUserDataCommand', '<c:url value='/uzivatelsky-ucet/' />')" /></td>
		</tr>
	</table>

	<%-- Highlights user's data (more precisely '<form:input>') which were incorrectly provided. --%>
	<%-- ATTENTION: The tag <script> has to start at the beginning of the line. --%>
	<script type="text/javascript">
<!--
	<%-- ATTENTION: The tag <form:errors> can return a text with quotation marks \";
		hence apostrophes are used as wrapping signs. --%>
	var passwordErrors = '<form:errors path="newPassword" />';
	if (passwordErrors != "") {
		$("#newPassword").addClass("errorInput");
	}
	var confirmationErrors = '<form:errors path="newPasswordConfirmation" />';
	if (confirmationErrors != "") {
		$("#newPasswordConfirmation").addClass("errorInput");
	}
	var emailErrors = '<form:errors path="user.email" />';
	if (emailErrors != "") {
		$("#user\\.email").addClass("errorInput");
	}
	var phoneErrors = '<form:errors path="user.phone" />';
	if (phoneErrors != "") {
		$("#user\\.phone").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#newPassword").focus();
//-->
</script>
