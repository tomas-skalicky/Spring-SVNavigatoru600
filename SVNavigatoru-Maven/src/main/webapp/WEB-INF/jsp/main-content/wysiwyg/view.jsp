<%-- Statically included (= copied) to /board/view.jsp, /remostav/contact/view.jsp and some other pages. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.domain.WysiwygSection"%>

<spring:eval expression="section.sourceCode" />

<p>
	<small><spring:message code="edit.updated" />: <em>
			<%
			Locale locale = Localization.getLocale(request);
			WysiwygSection section = (WysiwygSection) request.getAttribute("section");
			String formattedDate = DateUtils.format(section.getLastSaveTime(), DateUtils.DEFAULT_DATE_TIME_FORMATS.get(locale), locale);
			%><%=formattedDate%> <%-- The format how the time is written out is determined by the field of the WysiwygSection class. --%>
			<%-- 		<spring:eval expression="section.lastSaveTime" /> --%> </em> </small>
</p>

<%if (UserUtils.isLogged() && UserUtils.getLoggedUser().canEditNews()) {%>
<p>
	<a href="<c:url value="/${viewAddress}/editace/#editor" />"><spring:message code="edit.edit" />
	</a>
</p>
<%}%>
