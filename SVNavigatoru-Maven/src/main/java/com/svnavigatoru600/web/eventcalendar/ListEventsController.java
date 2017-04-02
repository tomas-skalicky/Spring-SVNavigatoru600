package com.svnavigatoru600.web.eventcalendar;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.url.eventcalendar.EventsUrlParts;
import com.svnavigatoru600.viewmodel.eventcalendar.ShowAllEvents;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListEventsController extends AbstractEventController {

    /**
     * Command used in /main-content/event-calendar/list-events.jsp.
     */
    public static final String COMMAND = "showAllEventsCommand";

    @Inject
    public ListEventsController(final CalendarEventService eventService, final MessageSource messageSource) {
        super(eventService, messageSource);
    }

    @GetMapping(value = EventsUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {

        final ShowAllEvents command = new ShowAllEvents();

        final List<CalendarEvent> events = getEventService().findAllFutureEventsOrdered();
        command.setEvents(events);

        // Sets up all auxiliary (but necessary) maps.
        command.setLocalizedDeleteQuestions(
                CalendarEventService.getLocalizedDeleteQuestions(events, request, getMessageSource()));

        model.addAttribute(ListEventsController.COMMAND, command);
        return PageViewsEnum.LIST.getViewName();
    }

    @GetMapping(value = EventsUrlParts.CREATED_URL)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllEvents) model.get(ListEventsController.COMMAND)).setEventCreated(true);
        return view;
    }

    @GetMapping(value = EventsUrlParts.DELETED_URL)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        final String view = initPage(request, model);
        ((ShowAllEvents) model.get(ListEventsController.COMMAND)).setEventDeleted(true);
        return view;
    }
}
