<%-- Statically included (= copied) to /board/edit.jsp, /remostav/contact/edit.jsp and some other pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<form:form action="IS_FILLED_BY_JAVASCRIPT" commandName="wysiwygSectionEditCommand" method="POST">
	<form:errors path="*" cssClass="error" element="p" />

	<a name="editor"></a>
	<%-- No autocomplete attribute available in the form:textarea element.
		The value of the element is always completed automatically. --%>
	<form:textarea path="sourceCode" cssClass="standaloneWysiwygEditor" cols="20" rows="20" />

	<input type="submit" value="<spring:message code="edit.save-changes-and-finish-editing" />"
		onclick="setFormAction('wysiwygSectionEditCommand', '<c:url value='/${viewAddress}/editace/ulozit-a-skoncit/' />')" />
	<input type="submit" value="<spring:message code="edit.cancel-changes-and-finish-editing" />"
		onclick="setFormAction('wysiwygSectionEditCommand', '<c:url value='/${viewAddress}/editace/neukladat-a-skoncit/' />')" />
</form:form>

<script type="text/javascript">
<!--
	<%-- ATTENTION: "action" has to be set this way. Otherwise, the save button of the WYSIWYG editor would not work. --%>
	setFormAction('wysiwygSectionEditCommand', '<c:url value='/${viewAddress}/editace/ulozit/#editor' />');
//-->
</script>
