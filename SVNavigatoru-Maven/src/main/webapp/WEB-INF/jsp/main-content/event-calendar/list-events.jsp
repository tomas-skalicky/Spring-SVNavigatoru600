<%-- For more details about the concepts used here, see the /users/administration/list-users.jsp file. --%>

<%@ page pageEncoding="UTF-8"%>
<%@ include file="../../include-preceding-html.jsp"%>

<%@ page import="com.svnavigatoru600.viewmodel.eventcalendar.ShowAllEvents"%>
<%@ page import="com.svnavigatoru600.web.eventcalendar.ListEventsController"%>

<c:if test="${showAllEventsCommand.eventCreated}">
	<p class="successfulOperation">
		<spring:message code="event-calendar.event-has-been-successfully-added" />
	</p>
</c:if>
<c:if test="${showAllEventsCommand.eventDeleted}">
	<p class="successfulOperation">
		<spring:message code="event-calendar.event-has-been-successfully-deleted" />
	</p>
</c:if>
<c:if test="${not empty error}">
	<p class="error">
		<spring:message code="${error}" />
	</p>
</c:if>

<c:set var="sectionUrl" value="kalendar-akci" />
<my:loggedUser checkIfCanEditEvents="true">
	<p>
		<strong><a href="<c:url value="/${sectionUrl}/novy/" />"><spring:message
					code="event-calendar.add-new-event" /></a></strong>
	</p>
</my:loggedUser>



<%
// Gets the command from the ModelMap.
ShowAllEvents command = (ShowAllEvents) request.getAttribute(ListEventsController.COMMAND);
List<CalendarEvent> events = command.getEvents();
if (events.size() > 0) {
%>
<div class="block">
	<%}

String homeUrl = request.getContextPath();
Locale locale = Localization.getLocale(request);
for (CalendarEvent event : events) {
%>
	<div class="box">
		<div class="<%=event.getTypedPriority().getCssClass()%> small">
			<div class="titlewrap">
				<% String formattedDate = DateUtils.format(event.getDate(), DateUtils.MIDDLE_DATE_FORMATS.get(locale), locale); %>
				<h4>
					<span><%=event.getName()%> <small><em><%=formattedDate%></em></small></span>
				</h4>


				<%-- Administration of events --%>
				<my:loggedUser checkIfCanEditEvents="true">
					<p class="controls">
						<% int eventId = event.getId(); %>
						<%-- Edit icon --%>
						<a href="<c:out value="${homeUrl}" />/<c:out value="${sectionUrl}" />/existujici/<%=eventId%>/"
							title="<spring:message code="event-calendar.modify-event" />" class="edit"></a>
	
						<%-- Delete icon --%>
						<a id="delete[<%=eventId%>]" href="#" title="<spring:message code="event-calendar.delete-event" />" class="delete"
							onclick="if (confirm('<%=command.getLocalizedDeleteQuestions().get(event)%>')) {
											document.getElementById('delete[<%=eventId%>]').setAttribute('href', '<%=homeUrl
												%>/<c:out value="${sectionUrl}" />/existujici/<%=eventId%>/smazat/'); }"></a>
					</p>
					<div class="clearfix"></div>
				</my:loggedUser>
			</div>


			<div class="wrapleft">
				<div class="wrapright">
					<div class="tr">
						<div class="bl">
							<div class="tl">
								<div class="br the-content">
									<p><%=event.getDescription()%></p>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<%}

if (events.size() > 0) {
%>
</div>
<%}%>
