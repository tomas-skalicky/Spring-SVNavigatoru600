<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>


<c:set var="newsId" value="template" />
<div id="post_${newsId}" class="post invisible">
	<div class="post-header">
		<h3></h3>
		<p class="post-date">
			<span class="month"></span>
			<span class="day"></span>
		</p>
		<p class="post-author"></p>


		<%-- Administration of events --%>
		<c:if test="${canLoggedUserEditNews}">
			<p class="controls">
				<%-- Edit icon --%>
				<a id="edit[${newsId}]" href="#" title="<spring:message code="news.modify-news" />" class="edit" onclick=""></a>

				<%-- Delete icon --%>
				<a id="delete[${newsId}]" href="#" title="<spring:message code="news.delete-news" />" class="delete" onclick=""></a>
			</p>
			<div class="clearfix"></div>
		</c:if>

	</div>
	<div class="post-content clearfix"></div>
</div>
