<%-- For more details about the concepts used here, see the /users/administration/authority-administration.jsp file. --%>

<%-- Statically included (= copied) to /event-calendar/new-event.jsp and /event-calendar/edit-event.jsp JSP pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.web.eventcalendar.AbstractNewEditEventController"%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditEventCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="event.id" />

	<table>
		<tr>
			<td><form:label path="event.name"><spring:message code="event-calendar.name" />: *</form:label></td>
			<td><form:input path="event.name" autocomplete="${newEditEventCommand.event.name}" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="event.date"><spring:message code="event-calendar.date" />: *</form:label></td>
			<td><form:input path="event.date" autocomplete="${newEditEventCommand.event.date}" /> <script
					type='text/javascript'>
					<%String homeUrl = request.getContextPath();%>
					new Calendar({'formName': '<%=AbstractNewEditEventController.COMMAND%>', 'controlName': 'event.date',
						'id': 'event.date_calendar', 'imgPath': '<%=homeUrl%>/tigra_calendar/imgs/'});
				</script></td>
		</tr>
		<tr>
			<td><form:label path="event.description"><spring:message code="event-calendar.description" />:</form:label></td>
			<td><form:textarea path="event.description" cols="20" rows="3" /></td>
		</tr>
		<tr>
			<td><form:label path="newPriority"><spring:message code="event-calendar.priority" />:</form:label></td>
			<td><ul>
					<form:select path="newPriority" items="${priorityTypeList}" element="li" />
				</ul></td>
		</tr>


		<tr>
			<td><span class="formNote">* <spring:message code="required-data" /></span></td>
			<c:set var="successUrl" value="/kalendar-akci/${successUrlEnd}" />
			<td><input type="submit" value="<spring:message code="${buttonTittle}" />"
				onclick="setFormAction('<%=AbstractNewEditEventController.COMMAND%>', '<c:url value='${successUrl}' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var nameErrors = '<form:errors path="event.name" />';
	if (nameErrors != "") {
		$("#event\\.name").addClass("errorInput");
	}
	var dateErrors = '<form:errors path="event.date" />';
	if (dateErrors != "") {
		$("#event\\.date").addClass("errorInput");
	}
	var descriptionErrors = '<form:errors path="event.description" />';
	if (descriptionErrors != "") {
		$("#event\\.description").addClass("errorInput");
	}
	var priorityErrors = '<form:errors path="newPriority" />';
	if (priorityErrors != "") {
		$("#newPriority").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#event\\.name").focus();
//-->
</script>
