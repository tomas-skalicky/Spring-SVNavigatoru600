package com.svnavigatoru600.web.eventcalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.eventcalendar.ShowAllEvents;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListEventsController extends AbstractEventController {

    private static final String REQUEST_MAPPING_BASE_URL = ListEventsController.BASE_URL;
    /**
     * Command used in /main-content/event-calendar/list-events.jsp.
     */
    public static final String COMMAND = "showAllEventsCommand";

    /**
     * Constructor.
     */
    @Inject
    public ListEventsController(CalendarEventService eventService, MessageSource messageSource) {
        super(eventService, messageSource);
    }

    @RequestMapping(value = ListEventsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {

        ShowAllEvents command = new ShowAllEvents();

        List<CalendarEvent> events = this.eventService.findAllFutureEventsOrdered();
        command.setEvents(events);

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(events, request));

        model.addAttribute(ListEventsController.COMMAND, command);
        return PageViews.LIST.getViewName();
    }

    @RequestMapping(value = ListEventsController.REQUEST_MAPPING_BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = this.initPage(request, model);
        ((ShowAllEvents) model.get(ListEventsController.COMMAND)).setEventCreated(true);
        return view;
    }

    @RequestMapping(value = ListEventsController.REQUEST_MAPPING_BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = this.initPage(request, model);
        ((ShowAllEvents) model.get(ListEventsController.COMMAND)).setEventDeleted(true);
        return view;
    }

    /**
     * Gets a {@link Map} which for each input {@link CalendarEvent} contains an appropriate localized delete
     * questions.
     */
    private Map<CalendarEvent, String> getLocalizedDeleteQuestions(List<CalendarEvent> events,
            HttpServletRequest request) {
        final String messageCode = "event-calendar.do-you-really-want-to-delete-event";
        Map<CalendarEvent, String> questions = new HashMap<CalendarEvent, String>();

        for (CalendarEvent event : events) {
            Object[] messageParams = new Object[] { event.getName() };
            questions.put(event,
                    Localization.findLocaleMessage(this.messageSource, request, messageCode, messageParams));
        }
        return questions;
    }
}
