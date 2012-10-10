<%-- Statically included (= copied) to new-user.jsp and edit-user.jsp. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../../include-preceding-html.jsp"%>

<%@ page import="svnavigatoru.service.users.AdministrateUserData"%>
<%@ page import="svnavigatoru.web.users.administration.NewEditUserController"%>

<%
// Gets the command from the ModelMap.
AdministrateUserData command = (AdministrateUserData) request.getAttribute(NewEditUserController.COMMAND);
int roleCounter = 0;
%>
<c:forEach items="${newAuthorities}" var="roleCheck">
	<%-- Checkboxes are discussed in
		http://www.mkyong.com/spring-mvc/spring-mvc-checkbox-and-checkboxes-example/. --%>
	<%
	Map<Integer, String> checkboxIds = command.getRoleCheckboxId();
	String checkboxId = checkboxIds.get(roleCounter);
	String checkboxTitle = command.getLocalizedRoleCheckboxTitles().get(roleCounter);

	if (roleCounter == AuthorityType.ROLE_MEMBER_OF_SV.ordinal()) {
		String boardCheckboxId = checkboxIds.get(AuthorityType.ROLE_MEMBER_OF_BOARD.ordinal());
	%>
	<li><input type="checkbox" id="<%=checkboxId%>" name="<%=checkboxId%>" class="checkbox"
		<c:if test="${roleCheck == true}">checked="checked"</c:if>
		onchange="syncRoleCheckboxes('<%=checkboxId%>', '<%=boardCheckboxId%>', '<%=checkboxId%>');" /> <label
		for="<%=checkboxId%>"><%=checkboxTitle%></label></li>
	<%
	} else if (roleCounter == AuthorityType.ROLE_MEMBER_OF_BOARD.ordinal()) {
		String svCheckboxId = checkboxIds.get(AuthorityType.ROLE_MEMBER_OF_SV.ordinal());
	%>
	<li><input type="checkbox" id="<%=checkboxId%>" name="<%=checkboxId%>" class="checkbox"
		<c:if test="${roleCheck == true}">checked="checked"</c:if>
		onchange="syncRoleCheckboxes('<%=svCheckboxId%>', '<%=checkboxId%>', '<%=checkboxId%>');" /> <label
		for="<%=checkboxId%>"><%=checkboxTitle%></label></li>
	<%
	} else if (roleCounter != AuthorityType.ROLE_REGISTERED_USER.ordinal()) {
	%>
	<li><input type="checkbox" id="<%=checkboxId%>" name="<%=checkboxId%>" class="checkbox"
		<c:if test="${roleCheck == true}">checked="checked"</c:if> /> <label for="<%=checkboxId%>"><%=checkboxTitle%></label>
	</li>
	<%}
	++roleCounter;
	%>
</c:forEach>
