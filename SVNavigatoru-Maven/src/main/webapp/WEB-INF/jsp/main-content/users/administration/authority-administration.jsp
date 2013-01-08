<%-- Statically included (= copied) to new-user.jsp and edit-user.jsp --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.users.AdministrateUserData"%>
<%@ page import="com.svnavigatoru600.web.users.administration.AbstractNewEditUserController"%>

<%-- Constants --%>
<spring:eval expression="T(com.svnavigatoru600.domain.users.AuthorityType).ROLE_MEMBER_OF_SV.ordinal" var="memberOfSvOrdinal" />
<spring:eval expression="T(com.svnavigatoru600.domain.users.AuthorityType).ROLE_MEMBER_OF_BOARD.ordinal" var="memberOfBoardOrdinal" />

<c:forEach items="${newAuthorities}" var="roleCheck" varStatus="roleStatus">
	<%-- Checkboxes are discussed in http://www.mkyong.com/spring-mvc/spring-mvc-checkbox-and-checkboxes-example/. --%>
	<c:set var="roleCounter" value="${0 + roleStatus.index}" />
	<c:set var="checkboxId" value="${roleCheckboxId[roleCounter]}" />
	<c:set var="checkboxTitle" value="${localizedRoleCheckboxTitles[roleCounter]}" />
	
	<%-- ROLE_MEMBER_OF_SV --%>
	<spring:eval expression="${roleCounter} == T(com.svnavigatoru600.domain.users.AuthorityType).ROLE_MEMBER_OF_SV.ordinal" var="isMemberOfSv" />
	<c:if test="${isMemberOfSv}">
		<li><input type="checkbox" id="${checkboxId}" name="${checkboxId}" class="checkbox"
			<c:if test="${roleCheck == true}">checked="checked"</c:if>
			onchange="syncRoleCheckboxes('${checkboxId}', '${roleCheckboxId[memberOfBoardOrdinal]}', '${checkboxId}');" /> <label
			for="${checkboxId}">${checkboxTitle}</label></li>
	</c:if>
	
	<%-- ROLE_MEMBER_OF_BOARD --%>
	<spring:eval expression="${roleCounter} == T(com.svnavigatoru600.domain.users.AuthorityType).ROLE_MEMBER_OF_BOARD.ordinal" var="isMemberOfBoard" />
	<c:if test="${isMemberOfBoard}">
		<li><input type="checkbox" id="${checkboxId}" name="${checkboxId}" class="checkbox"
			<c:if test="${roleCheck == true}">checked="checked"</c:if>
			onchange="syncRoleCheckboxes('${roleCheckboxId[memberOfSvOrdinal]}', '${checkboxId}', '${checkboxId}');" /> <label
			for="${checkboxId}">${checkboxTitle}</label></li>
	</c:if>
	
	<%-- the other except ROLE_REGISTERED_USER --%>
	<spring:eval expression="${roleCounter} == T(com.svnavigatoru600.domain.users.AuthorityType).ROLE_REGISTERED_USER.ordinal" var="isRegisteredUser" />
	<c:if test="${!isMemberOfSv && !isMemberOfBoard && !isRegisteredUser}">
		<li><input type="checkbox" id="${checkboxId}" name="${checkboxId}" class="checkbox"
			<c:if test="${roleCheck == true}">checked="checked"</c:if> /> <label for="${checkboxId}">${checkboxTitle}</label></li>
	</c:if>
</c:forEach>
