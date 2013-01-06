<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%-- For more details about the concepts used here, see the forgotten-password.jsp file. --%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="userCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="user.enabled" />

	<table>
		<%-- Login data --%>
		<tr>
			<td><form:label path="user.username"><spring:message code="username" />: *</form:label></td>
			<td><form:input path="user.username" autocomplete="${userCommand.user.username}" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="newPassword"><spring:message code="password" />: *</form:label></td>
			<td><form:input path="newPassword" autocomplete="${userCommand.newPassword}" class="longText" /> <a href="#"
				onclick="$('#newPassword').val(generatePassword(8));"><spring:message code="user-administration.generate" />
			</a></td>
		</tr>


		<%-- Other user's data --%>
		<tr>
			<td><form:label path="user.firstName"><spring:message code="first-name" />: *</form:label></td>
			<td><form:input path="user.firstName" autocomplete="${userCommand.user.firstName}" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="user.lastName"><spring:message code="last-name" />: *</form:label></td>
			<td><form:input path="user.lastName" autocomplete="${userCommand.user.lastName}" class="longText" />
			</td>
		</tr>
		<tr>
			<td><form:label path="user.email"><spring:message code="email" />:</form:label></td>
			<td><form:input path="user.email" autocomplete="${userCommand.user.email}" class="longText" /></td>
		</tr>
		<tr>
			<td><spring:message code="user-roles" />: *</td>
			<td>
				<ul>
					<c:set var="newAuthorities" value="${userCommand.newAuthorities}" />
					<%@ include file="/WEB-INF/jsp/main-content/users/administration/authority-administration.jsp"%>
				</ul></td>
		</tr>


		<tr>
			<td><span class="formNote">* <spring:message code="required-data" /></span></td>
			<td><input type="submit" value="<spring:message code="user-administration.create-user" />"
				onclick="setFormAction('userCommand', '<c:url value='/administrace-uzivatelu/novy/' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var usernameErrors = '<form:errors path="user.username" />';
	if (usernameErrors != "") {
		$("#user\\.username").addClass("errorInput");
	}
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
	var emailErrors = '<form:errors path="user.email" />';
	if (emailErrors != "") {
		$("#user\\.email").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#user\\.username").focus();
//-->
</script>
