package com.svnavigatoru600.viewmodel.eventcalendar;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Used in the {@link AbstractPrivateSectionMetaController}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class EventWrapper {

    private CalendarEvent event = null;
    private String middleDateFormattedDate = null;

    public EventWrapper(final CalendarEvent event, final HttpServletRequest request) {
        this.event = event;
        Locale locale = Localization.getLocale(request);
        this.middleDateFormattedDate = DateUtils.format(event.getDate(),
                DateUtils.MIDDLE_DATE_FORMATS.get(locale), locale);
    }

    public CalendarEvent getEvent() {
        return this.event;
    }

    public void setEvent(final CalendarEvent event) {
        this.event = event;
    }

    public String getMiddleDateFormattedDate() {
        return this.middleDateFormattedDate;
    }

    public void setMiddleDateFormattedDate(final String middleDateFormattedDate) {
        this.middleDateFormattedDate = middleDateFormattedDate;
    }
}
