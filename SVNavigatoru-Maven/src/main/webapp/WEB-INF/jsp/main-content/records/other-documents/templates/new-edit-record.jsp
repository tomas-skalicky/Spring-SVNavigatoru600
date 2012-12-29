<%-- For more details about the concepts used here, see the users/administration/authority-administration.jsp file. --%>

<%-- Statically included (= copied) to /records/other-documents/remostav/new-record.jsp,
	/records/other-documents/remostav/edit-record.jsp and to some other JSP pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.records.otherdocuments.NewEditRecord"%>
<%@ page import="com.svnavigatoru600.web.records.otherdocuments.NewEditDocumentController"%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditRecordCommand" method="POST"
	enctype="multipart/form-data">
	<form:errors path="*" cssClass="error" element="p" />
	<form:hidden path="record.id" />
	<form:hidden path="record.creationTime" />

	<table>
		<tr>
			<td><form:label path="record.name">
					<spring:message code="other-documents.document-name" />:</form:label>
			</td>
			<td><form:input path="record.name" autocomplete="${newEditRecordCommand.record.name}" class="longText" />
			</td>
		</tr>
		<tr>
			<td><form:label path="record.description">
					<spring:message code="other-documents.document-description" />:</form:label>
			</td>
			<td><form:textarea path="record.description" cols="20" rows="3" />*</td>
		</tr>
		<tr>
			<td><label for="newFile"><spring:message code="attached-file" />:</label>
			</td>
			<td><c:if test="${not empty newEditRecordCommand.record.fileName}">
					<%@ include file="/WEB-INF/jsp/main-content/records/other-documents/templates/edit-file.jsp"%>
				</c:if> <c:if test="${empty newEditRecordCommand.record.fileName}">
					<%@ include file="/WEB-INF/jsp/main-content/records/new-file.jsp"%>
				</c:if></td>
		</tr>
		<tr>
			<td><spring:message code="other-documents.areas-which-the-document-reaches" />:</td>
			<td>
				<ul>
					<%
					// Gets the command from the ModelMap.
					NewEditRecord command = (NewEditRecord) request.getAttribute(NewEditDocumentController.COMMAND);
					int typeCounter = 0;
					%>
					<c:forEach items="${newEditRecordCommand.newTypes}" var="typeCheck">
						<%
						String checkboxId = command.getTypeCheckboxId().get(typeCounter);
						String checkboxTitle = command.getLocalizedTypeCheckboxTitles().get(typeCounter);
						%>
						<li><input type="checkbox" id="<%=checkboxId%>" name="<%=checkboxId%>" class="checkbox"
							<c:if test="${typeCheck == true}">checked="checked"</c:if> /> <label for="<%=checkboxId%>"><%=checkboxTitle%></label>
						</li>
						<% ++typeCounter; %>
					</c:forEach>
				</ul></td>
		</tr>


		<tr>
			<td><span class="formNote">* <spring:message code="optional-data" />
			</span>
			</td>
			<td><input type="submit" value="<spring:message code="${buttonTittle}" />"
				onclick="setFormAction('<%=NewEditDocumentController.COMMAND%>', '<c:url value='${successUrl}' />')" /></td>
		</tr>
	</table>

	<script type="text/javascript">
<!--
	var nameErrors = '<form:errors path="record.name" />';
	if (nameErrors != "") {
		$("#record\\.name").addClass("errorInput");
	}
	var descriptionErrors = '<form:errors path="record.description" />';
	if (descriptionErrors != "") {
		$("#record\\.description").addClass("errorInput");
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
	$("#record\\.name").focus();
//-->
</script>
