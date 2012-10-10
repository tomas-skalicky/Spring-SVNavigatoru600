<%@ page pageEncoding="UTF-8"%>
<%@ include file="../include-preceding-html.jsp"%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="sendNewPasswordCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />

	<table>
		<tr>
			<td><form:label path="user.email">
					<spring:message code="email" />:</form:label>
			</td>
			<td><form:input path="user.email" autocomplete="${sendNewPasswordCommand.user.email}" class="longText" />
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="<spring:message code="login.send-new-password" />" />
			</td>
		</tr>
	</table>

	<%-- Administrators (at least their necessary person data) need to be stored in case the input email address
		is not valid. Therefore, c:forEach.varStatus and spring:bind is used. --%>
	<spring:message code="login.contact-administrator-if-you-have-no-email" />:
	<p>
		<c:forEach items="${sendNewPasswordCommand.administrators}" var="admin" varStatus="adminCounter">
			<br />
			<em> <c:out value="${admin.fullName}" /> <%-- First names --%> <spring:bind
					path="administrators[${adminCounter.index}].firstName">
					<input type="hidden" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
				</spring:bind> <%-- Last names --%> <spring:bind path="administrators[${adminCounter.index}].lastName">
					<input type="hidden" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}" />" />
				</spring:bind> <%-- Phones --%> <c:if test="${not empty admin.phone}">
					<spring:bind path="administrators[${adminCounter.index}].phone">
						, <spring:message code="phone.abbreviation" />. <c:out value="${admin.phone}" />
						<input type="hidden" name="<c:out value="${status.expression}" />" value="<c:out value="${status.value}"/>" />
					</spring:bind>
				</c:if> </em>
		</c:forEach>
	</p>

	<%-- Highlights user's data (more precisely '<form:input>') which were incorrectly provided. --%>
	<%-- ATTENTION: The tag <script> has to start at the beginning of the line. --%>
	<script type="text/javascript">
<!--
	<%-- ATTENTION: The tag <form:errors> can return a text with quotation marks \";
		hence apostrophes are used as wrapping signs. --%>
	var emailErrors = '<form:errors path="user.email" />';
	if (emailErrors != "") {
		$("#user\\.email").addClass("errorInput");
	}
//-->
</script>
</form:form>



<%-- Helps to focus the email field during the loading of the page. <c:out> --%>
<script type="text/javascript">
<!--
	<%-- The jQuery command below and 'document.getElementById("user.email").focus();' are the same. --%>
	$("#user\\.email").focus();
	
	<%--
	<% String homeUrl = request.getContextPath() + "/"; %>
	var action = "<%=homeUrl%>prihlaseni/poslat-nove-heslo";
	--%>
	
	<%-- The <c:url> tag finds the correct URL more elegantly than when we would use the request.getContextPath function. --%>
	setFormAction('sendNewPasswordCommand', '<c:url value='/prihlaseni/poslat-nove-heslo/' />');
//-->
</script>
