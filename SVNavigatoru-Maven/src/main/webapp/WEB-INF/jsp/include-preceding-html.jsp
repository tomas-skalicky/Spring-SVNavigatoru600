<%@ page session="true"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<%@ taglib prefix="my" uri="/WEB-INF/tld/my.tld"%>


<%@ page import="java.util.Date"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Locale"%>
<%@ page import="java.util.Map"%>

<%@ page import="org.springframework.context.MessageSource"%>

<%@ page import="com.svnavigatoru600.domain.News"%>
<%@ page import="com.svnavigatoru600.domain.eventcalendar.CalendarEvent"%>
<%@ page import="com.svnavigatoru600.domain.eventcalendar.PriorityTypeEnum"%>
<%@ page import="com.svnavigatoru600.domain.forum.ForumContribution"%>
<%@ page import="com.svnavigatoru600.domain.forum.ForumThread"%>
<%@ page import="com.svnavigatoru600.domain.records.OtherDocumentRecord"%>
<%@ page import="com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum"%>
<%@ page import="com.svnavigatoru600.domain.records.SessionRecord"%>
<%@ page import="com.svnavigatoru600.domain.records.SessionRecordTypeEnum"%>
<%@ page import="com.svnavigatoru600.domain.users.AuthorityTypeEnum"%>
<%@ page import="com.svnavigatoru600.domain.users.User"%>

<%@ page import="com.svnavigatoru600.service.util.DateUtils"%>
<%@ page import="com.svnavigatoru600.service.util.JspCodeGenerator"%>
<%@ page import="com.svnavigatoru600.service.util.Localization"%>
<%@ page import="com.svnavigatoru600.service.util.UserUtils"%>

<%@ page import="com.svnavigatoru600.common.constants.CommonConstants"%>
