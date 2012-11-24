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

<%@ page import="com.svnavigatoru600.domain.News"%>
<%@ page import="com.svnavigatoru600.domain.eventcalendar.CalendarEvent"%>
<%@ page import="com.svnavigatoru600.domain.eventcalendar.PriorityType"%>
<%@ page import="com.svnavigatoru600.domain.forum.Contribution"%>
<%@ page import="com.svnavigatoru600.domain.forum.Thread"%>
<%@ page import="com.svnavigatoru600.domain.records.OtherDocumentRecord"%>
<%@ page import="com.svnavigatoru600.domain.records.OtherDocumentRecordType"%>
<%@ page import="com.svnavigatoru600.domain.records.SessionRecord"%>
<%@ page import="com.svnavigatoru600.domain.records.SessionRecordType"%>
<%@ page import="com.svnavigatoru600.domain.users.AuthorityType"%>
<%@ page import="com.svnavigatoru600.domain.users.User"%>

<%@ page import="com.svnavigatoru600.service.util.DateUtils"%>
<%@ page import="com.svnavigatoru600.service.util.JspCodeGenerator"%>
<%@ page import="com.svnavigatoru600.service.util.Localization"%>
<%@ page import="com.svnavigatoru600.service.util.UserUtils"%>

<%@ page import="com.svnavigatoru600.web.Configuration"%>
