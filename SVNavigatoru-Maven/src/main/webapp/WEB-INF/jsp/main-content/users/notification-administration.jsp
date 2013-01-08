<%-- Statically included (= copied) to new-user.jsp and edit-user.jsp. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

	
<%-- IN_NEWS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_NEWS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToNews" class="checkbox" /> <form:label
	path="user.subscribedToNews">${checkboxTitle}</form:label></li>

<%-- IN_EVENTS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_EVENTS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToEvents" class="checkbox" /> <form:label
	path="user.subscribedToEvents">${checkboxTitle}</form:label></li>

<%-- IN_FORUM --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_FORUM.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToForum" class="checkbox" /> <form:label
	path="user.subscribedToForum">${checkboxTitle}</form:label></li>

<%-- IN_OTHER_DOCUMENTS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_OTHER_DOCUMENTS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToOtherDocuments" class="checkbox" /> <form:label
	path="user.subscribedToNews">${checkboxTitle}</form:label></li>

<%-- IN_OTHER_SECTIONS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_OTHER_SECTIONS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToOtherSections" class="checkbox" /> <form:label
	path="user.subscribedToNews">${checkboxTitle}</form:label></li>
