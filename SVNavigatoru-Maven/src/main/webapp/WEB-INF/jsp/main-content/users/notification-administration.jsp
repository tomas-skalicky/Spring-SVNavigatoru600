<%-- Statically included (= copied) to new-user.jsp and edit-user.jsp. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<%-- Constants --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_NEWS.ordinal" var="subscribedToNewsOrdinal" />
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_EVENTS.ordinal" var="subscribedToEventsOrdinal" />
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_FORUM.ordinal" var="subscribedToForumOrdinal" />
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_OTHER_DOCUMENTS.ordinal" var="subscribedToOtherDocumentsOrdinal" />
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_OTHER_SECTIONS.ordinal" var="subscribedToOtherSectionsOrdinal" />
	
<%-- ROLE_MEMBER_OF_SV --%>
<c:out value="${user.subscribedToNews}" />
<%-- <li><input type="checkbox" id="${checkboxId}" name="${checkboxId}" class="checkbox" --%>
<%-- 	<c:if test="${roleCheck == true}">checked="checked"</c:if> /> <label for="${checkboxId}">${checkboxTitle}</label></li> --%>
