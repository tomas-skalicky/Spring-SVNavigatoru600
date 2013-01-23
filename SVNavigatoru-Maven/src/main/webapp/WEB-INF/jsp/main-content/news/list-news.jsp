<%-- For more details about the concepts used here, see the /users/administration/list-users.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.news.ShowAllNews"%>
<%@ page import="com.svnavigatoru600.web.news.ListNewsController"%>



<%-------- Messages --------%>
<p id="successAddMessage" class="successfulOperation invisible">
	<spring:message code="news.news-has-been-successfully-added" />
</p>
<p id="successDeleteMessage" class="successfulOperation invisible">
	<spring:message code="news.news-has-been-successfully-deleted" />
</p>
<p id="successEditMessage" class="successfulOperation invisible">
	<spring:message code="edit.changes-successfully-saved" />
</p>
<p id="fatalErrors" class="error invisible"></p>
<%-------- /Messages --------%>



<spring:eval expression="T(com.svnavigatoru600.service.util.UserUtils).loggedUser.canEditNews()" var="canLoggedUserEditNews" />

<c:set var="sectionUrl" value="novinky" />
<c:if test="${canLoggedUserEditNews}">
	<p id="newNewsLink">
		<a href="<c:url value="/${sectionUrl}/novy/" />" onclick="goToNewNewsForm(); return false;">
			<strong><spring:message code="news.add-new-news" /></strong>
		</a>
	</p>
	<%@ include file="/WEB-INF/jsp/main-content/news/new-edit-news.jsp"%>
</c:if>



<div id="newsList">
<c:if test="${canLoggedUserEditNews}">
	<%-- For visualization of the new news via AJAX --%>
	<%@ include file="/WEB-INF/jsp/main-content/news/list-news-template.jsp"%>
</c:if>



<%
Locale locale = Localization.getLocale(request);

// Gets the command from the ModelMap.
ShowAllNews command = (ShowAllNews) request.getAttribute(ListNewsController.COMMAND);
List<News> newss = command.getNews();
for (News news : newss) {
%>
<article id="post_<%=news.getId()%>" class="post">
	<div class="post-header">
		<h3><%=news.getTitle()%></h3>
		<p class="post-date">
			<%
			Date creationTime = news.getCreationTime();
			String formattedMonth = DateUtils.format(creationTime, DateUtils.LONG_MONTH_FORMATS.get(locale), locale); %>
			<span class="month"><%=formattedMonth%></span>
			<% String formattedDay = DateUtils.format(creationTime, DateUtils.SHORT_DAY_FORMATS.get(locale), locale); %>
			<span class="day"><%=formattedDay%></span>
		</p>
		<p class="post-author"></p>


		<%-- Administration of events --%>
		<c:if test="${canLoggedUserEditNews}">
		<%
			int newsId = news.getId();
			%>
			<p class="controls">
				<c:url var="urlBeginning" value="/${sectionUrl}/existujici/" />
			
				<%-- Edit icon --%>
				<a id="edit[<%=newsId%>]" href="#" title="<spring:message code="news.modify-news" />" class="edit"
					onclick="goToEditNewsForm(<%=newsId%>, '<c:out value="${urlBeginning}" />'); return false;"></a>

				<%-- Delete icon --%>
				<%-- "return false" command in the "onclick" attribute helps to prevent a move of the user
					at the top of the page when he presses the Cancel button. --%>
				<a id="delete[<%=newsId%>]" href="#" title="<spring:message code="news.delete-news" />" class="delete"
					onclick="deleteNews(<%=newsId%>, '<%=command.getLocalizedDeleteQuestions().get(news)%>',
						'<c:out value="${urlBeginning}" />'); return false;"></a>
			</p>
			<div class="clearfix"></div>
		</c:if>

	</div>
	<div class="post-content clearfix"><%=news.getText()%></div>
</article>
<%}%>
</div><%-- /newsList --%>


<script type="text/javascript" src="<c:url value='${myJsHome}ajax/news.js' />"></script>
