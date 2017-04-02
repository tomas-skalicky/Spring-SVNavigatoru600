<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%-- For more details about the concepts used here, see the forgotten-password.jsp file. --%>

<c:if test="${userCommand.dataSaved}">
	<p class="successfulOperation">
		<spring:message code="edit.changes-successfully-saved" />
	</p>
</c:if>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="userCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="user.username" />

	<table>
		<%-- Login data --%>
		<tr>
			<td><form:label path="newPassword"><spring:message code="password.new" />:</form:label></td>
			<td><form:input path="newPassword" autocomplete="${userCommand.newPassword}" class="longText" /> <a href="#"
				onclick="$('#newPassword').val(generatePassword(8));"><spring:message code="user-administration.generate" />
			</a></td>
		</tr>
		<tr>
			<td colspan="2"><span class="formNote"><spring:message code="edit-user-data.password-will-not-change-if" /></span></td>
		</tr>


		<%-- Other user's data --%>
		<tr>
			<td><form:label path="user.firstName"><spring:message code="first-name" />: *</form:label></td>
			<td><form:input path="user.firstName" autocomplete="${userCommand.user.firstName}" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="user.lastName"><spring:message code="last-name" />: *</form:label></td>
			<td><form:input path="user.lastName" autocomplete="${userCommand.user.lastName}" class="longText" /></td>
		</tr>
		<tr>
			<td><spring:message code="user-roles" />:</td>
			<td>
				<ul>
					<!-- Properties of command bean cannot be accessed outside in the included file. -->
					<c:set var="newAuthorities" value="${userCommand.newAuthorities}" />
					<c:set var="roleCheckboxId" value="${userCommand.roleCheckboxId}" />
					<c:set var="localizedRoleCheckboxTitles" value="${userCommand.localizedRoleCheckboxTitles}" />
					<%@ include file="/WEB-INF/jsp/main-content/users/administration/authority-administration.jsp"%>
				</ul></td>
		</tr>


		<tr>
			<td><span class="formNote">* <spring:message code="required-data" /></span></td>
			<td><input type="submit" value="<spring:message code="edit.save-changes" />"
				onclick="setFormAction('userCommand',
					'<c:url value='/administrace-uzivatelu/existujici/${userCommand.user.username}/' />')" />
			</td>
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
	var firstNameErrors = '<form:errors path="user.firstName" />';
	if (firstNameErrors != "") {
		$("#user\\.firstName").addClass("errorInput");
	}
	var lastNameErrors = '<form:errors path="user.lastName" />';
	if (lastNameErrors != "") {
		$("#user\\.lastName").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#newPassword").focus();
//-->
</script>
