<%-- Statically included (= copied) to edit-my-account.jsp --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

	
<%-- IN_NEWS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_NEWS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToNews" class="checkbox" /> <form:label
	path="user.subscribedToNews" for="user.subscribedToNews1">${checkboxTitle}</form:label></li>

<%-- IN_EVENTS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_EVENTS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToEvents" class="checkbox" /> <form:label
	path="user.subscribedToEvents" for="user.subscribedToEvents1">${checkboxTitle}</form:label></li>

<%-- IN_FORUM --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_FORUM.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToForum" class="checkbox" /> <form:label
	path="user.subscribedToForum" for="user.subscribedToForum1">${checkboxTitle}</form:label></li>

<%-- IN_OTHER_DOCUMENTS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_OTHER_DOCUMENTS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToOtherDocuments" class="checkbox" /> <form:label
	path="user.subscribedToOtherDocuments" for="user.subscribedToOtherDocuments1">${checkboxTitle}</form:label></li>

<%-- IN_OTHER_SECTIONS --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.NotificationType).IN_OTHER_SECTIONS.ordinal" var="subscribedTo" />
<c:set var="checkboxTitle" value="${localizedNotificationCheckboxTitles[subscribedTo]}" />
<li><form:checkbox path="user.subscribedToOtherSections" class="checkbox" /> <form:label
	path="user.subscribedToOtherSections" for="user.subscribedToOtherSections1">${checkboxTitle}</form:label></li>
