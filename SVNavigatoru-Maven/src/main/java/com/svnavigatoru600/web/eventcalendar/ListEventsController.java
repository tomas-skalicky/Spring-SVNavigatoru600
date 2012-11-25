package com.svnavigatoru600.web.eventcalendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.eventcalendar.ShowAllEvents;
import com.svnavigatoru600.service.util.DateUtils;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.service.util.OrderType;


@Controller
public class ListEventsController extends EventController {

	private static final String REQUEST_MAPPING_BASE_URL = ListEventsController.BASE_URL;
	/**
	 * Command used in /main-content/event-calendar/list-events.jsp.
	 */
	public static final String COMMAND = "showAllEventsCommand";

	/**
	 * Constructor.
	 */
	@Autowired
	public ListEventsController(CalendarEventDao eventDao, MessageSource messageSource) {
		super(eventDao, messageSource);
	}

	@RequestMapping(value = ListEventsController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
	public String initPage(HttpServletRequest request, ModelMap model) {

		ShowAllEvents command = new ShowAllEvents();

		List<CalendarEvent> events = this.eventDao.findFutureEventsOrdered(DateUtils.getToday(), OrderType.ASCENDING);
		command.setEvents(events);

		// Sets up all auxiliary (but necessary) maps.
		command.setLocalizedDeleteQuestions(this.getLocalizedDeleteQuestions(events, request));

		model.addAttribute(ListEventsController.COMMAND, command);
		return PageViews.LIST.getViewName();
	}

	@RequestMapping(value = ListEventsController.REQUEST_MAPPING_BASE_URL + "vytvoreno/", method = RequestMethod.GET)
	public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
		String view = this.initPage(request, model);
		((ShowAllEvents) model.get(ListEventsController.COMMAND)).setEventCreated(true);
		return view;
	}

	@RequestMapping(value = ListEventsController.REQUEST_MAPPING_BASE_URL + "smazano/", method = RequestMethod.GET)
	public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
		String view = this.initPage(request, model);
		((ShowAllEvents) model.get(ListEventsController.COMMAND)).setEventDeleted(true);
		return view;
	}

	/**
	 * Gets a {@link Map} which for each input {@link CalendarEvent} contains an
	 * appropriate localized delete questions.
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