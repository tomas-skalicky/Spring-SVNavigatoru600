package com.svnavigatoru600.web.eventcalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
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
@Controller
public abstract class AbstractEventController extends AbstractPrivateSectionMetaController {

    protected static final String BASE_URL = "/kalendar-akci/";
    private CalendarEventService eventService;
    private MessageSource messageSource;

    public AbstractEventController(CalendarEventService eventService, MessageSource messageSource) {
        this.eventService = eventService;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected CalendarEventService getEventService() {
        return this.eventService;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return this.messageSource;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Nasty since the format is localized.
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.CALENDAR_DATE_FORMAT);

        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
