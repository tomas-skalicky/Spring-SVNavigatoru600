<%-- For more details about the concepts used here, see the /users/administration/authority-administration.jsp file. --%>

<%-- Statically included (= copied) to /news/list-news.jsp JSP pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>


<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="newEditNewsCommand" method="POST" class="invisible">
	<p id="inputErrors" class="error invisible"></p>
	<form:hidden path="news.id" />

	<table>
		<tr>
			<td><form:label path="news.title"><spring:message code="news.news-title" />:</form:label></td>
			<td><form:input path="news.title" class="longText" /></td>
		</tr>
		<tr>
			<td><form:label path="news.text"><spring:message code="news.text" />:</form:label></td>
			<td><form:textarea path="news.text" class="notStandaloneWysiwygEditor" cols="20" rows="10" /></td>
		</tr>

		<tr>
			<td></td>
			<td><input id="newsForm.submitButton" type="submit" value="IS_FILLED_BY_JAVASCRIPT" /></td>
		</tr>
	</table>
</form:form>
