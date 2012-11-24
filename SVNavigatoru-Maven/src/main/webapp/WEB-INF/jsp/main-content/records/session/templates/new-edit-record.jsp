<%-- For more details about the concepts used here, see the users/administration/authority-administration.jsp file. --%>

<%-- Statically included (= copied) to /records/session/remostav/new-record.jsp,
	/records/session/remostav/edit-record.jsp and to some other JSP pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.service.records.session.NewEditSessionRecord"%>
<%@ page import="com.svnavigatoru600.web.records.session.NewEditRecordController"%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditRecordCommand" method="POST"
	enctype="multipart/form-data">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="record.id" />

	<table>
		<tr>
			<td><form:label path="newType">
					<spring:message code="session-records.type-of-session" />:</form:label>
			</td>
			<td><ul>
					<form:radiobuttons path="newType" items="${sessionRecordTypeList}" element="li" />
				</ul>
			</td>
		</tr>
		<tr>
			<td><form:label path="record.sessionDate">
					<spring:message code="session-records.session-date" />:</form:label>
			</td>
			<td><form:input path="record.sessionDate" autocomplete="${newEditRecordCommand.record.sessionDate}" /> <script
					type='text/javascript'>
					<% String homeUrl = request.getContextPath(); %>
					new Calendar({'formName': '<%=NewEditRecordController.COMMAND%>', 'controlName': 'record.sessionDate',
						'id': 'record.sessionDate_calendar', 'imgPath': '<%=homeUrl%>/tigra_calendar/imgs/'});
				</script></td>
		</tr>
		<tr>
			<td><form:label path="record.discussedTopics">
					<spring:message code="session-records.discussed-topics" />:</form:label>
			</td>
			<td><form:textarea path="record.discussedTopics" cols="20" rows="3" />
			</td>
		</tr>
		<tr>
			<td><label for="newFile"><spring:message code="attached-file" />:</label>
			</td>
			<td><c:if test="${not empty newEditRecordCommand.record.fileName}">
					<%@ include file="/WEB-INF/jsp/main-content/records/session/templates/edit-file.jsp"%>
				</c:if> <c:if test="${empty newEditRecordCommand.record.fileName}">
					<%@ include file="/WEB-INF/jsp/main-content/records/new-file.jsp"%>
				</c:if></td>
		</tr>


		<tr>
			<td></td>
			<td><input type="submit" value="<spring:message code="${buttonTittle}" />"
				onclick="setFormAction('<%=NewEditRecordController.COMMAND%>', '<c:url value='${successUrl}' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var dateErrors = '<form:errors path="record.sessionDate" />';
	if (dateErrors != "") {
		$("#record\\.sessionDate").addClass("errorInput");
	}
	var topicsErrors = '<form:errors path="record.discussedTopics" />';
	if (topicsErrors != "") {
		$("#record\\.discussedTopics").addClass("errorInput");
	}
	var fileErrors = '<form:errors path="newFile" />';
	if (fileErrors != "") {
		$("#newFile").addClass("errorInput");
	}
	var fileNameErrors = '<form:errors path="record.fileName" />';
	if (fileNameErrors != "") {
		$("#record\\.fileName").addClass("errorInput");
	}
//-->
</script>
</form:form>



<script type="text/javascript">
<!--
	$("#record\\.discussedTopics").focus();
//-->
</script>
