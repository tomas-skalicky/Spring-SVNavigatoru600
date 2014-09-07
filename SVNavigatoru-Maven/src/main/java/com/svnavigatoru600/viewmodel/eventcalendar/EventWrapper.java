package com.svnavigatoru600.viewmodel.eventcalendar;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;

/**
 * Used in the {@link com.svnavigatoru600.web.AbstractPrivateSectionMetaController
 * AbstractPrivateSectionMetaController}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class EventWrapper {

    private CalendarEvent event = null;
    private String middleDateFormattedDate = null;

    public EventWrapper(CalendarEvent event, HttpServletRequest request) {
        this.event = event;
        Locale locale = Localization.getLocale(request);
        this.middleDateFormattedDate = DateUtils.format(event.getDate(),
                DateUtils.MIDDLE_DATE_FORMATS.get(locale), locale);
    }

    public CalendarEvent getEvent() {
        return this.event;
    }

    public void setEvent(CalendarEvent event) {
        this.event = event;
    }

    public String getMiddleDateFormattedDate() {
        return this.middleDateFormattedDate;
    }

    public void setMiddleDateFormattedDate(String middleDateFormattedDate) {
        this.middleDateFormattedDate = middleDateFormattedDate;
    }
}
