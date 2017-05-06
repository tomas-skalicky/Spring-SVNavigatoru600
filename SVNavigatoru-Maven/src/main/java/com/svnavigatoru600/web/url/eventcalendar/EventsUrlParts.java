package com.svnavigatoru600.web.url.eventcalendar;

import com.svnavigatoru600.web.url.CommonUrlParts;

/**
 * Contains snippets of URL which concern just web pages with only
 * {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvents}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class EventsUrlParts {

    public static final String BASE_URL = "/kalendar-akci/";
    public static final String NEW_URL = EventsUrlParts.BASE_URL + CommonUrlParts.NEW_EXTENSION;
    public static final String EXISTING_URL = EventsUrlParts.BASE_URL + CommonUrlParts.EXISTING_EXTENSION;
    public static final String CREATED_URL = EventsUrlParts.BASE_URL + CommonUrlParts.CREATED_EXTENSION;
    public static final String DELETED_URL = EventsUrlParts.BASE_URL + CommonUrlParts.DELETED_EXTENSION;

    private EventsUrlParts() {
    }
}
