package com.svnavigatoru600.web.eventcalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations upon the
 * {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvents}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractEventController extends AbstractPrivateSectionMetaController {

    private final CalendarEventService eventService;
    private final MessageSource messageSource;

    public AbstractEventController(final CalendarEventService eventService, final MessageSource messageSource) {
        this.eventService = eventService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected CalendarEventService getEventService() {
        return eventService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return messageSource;
    }

    @InitBinder
    public void initBinder(final WebDataBinder binder) {
        // Nasty since the format is localized.
        final SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.CALENDAR_DATE_FORMAT);

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
