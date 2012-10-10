<%@ page session="true"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>


<%@ page import="java.util.Date"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.Map"%>

<%@ page import="org.springframework.context.MessageSource"%>

<%@ page import="svnavigatoru.domain.News"%>
<%@ page import="svnavigatoru.domain.eventcalendar.CalendarEvent"%>
<%@ page import="svnavigatoru.domain.eventcalendar.PriorityType"%>
<%@ page import="svnavigatoru.domain.forum.Contribution"%>
<%@ page import="svnavigatoru.domain.forum.Thread"%>
<%@ page import="svnavigatoru.domain.records.OtherDocumentRecord"%>
<%@ page import="svnavigatoru.domain.records.OtherDocumentRecordType"%>
<%@ page import="svnavigatoru.domain.records.SessionRecord"%>
<%@ page import="svnavigatoru.domain.records.SessionRecordType"%>
<%@ page import="svnavigatoru.domain.users.AuthorityType"%>
<%@ page import="svnavigatoru.domain.users.User"%>

<%@ page import="svnavigatoru.service.util.DateUtils"%>
<%@ page import="svnavigatoru.service.util.JspCodeGenerator"%>
<%@ page import="svnavigatoru.service.util.Localization"%>
<%@ page import="svnavigatoru.service.util.UserUtils"%>

<%@ page import="svnavigatoru.web.Configuration"%>
