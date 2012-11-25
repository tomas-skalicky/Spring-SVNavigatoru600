package com.svnavigatoru600.web.eventcalendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.eventcalendar.EditEvent;
import com.svnavigatoru600.service.eventcalendar.EditEventValidator;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.Configuration;


@Controller
public class EditEventController extends NewEditEventController {

	private static final String REQUEST_MAPPING_BASE_URL = EditEventController.BASE_URL + "existujici/{eventId}/";
	/**
	 * Code of the error message used when the {@link DataAccessException} is
	 * thrown.
	 */
	public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

	/**
	 * Constructor.
	 */
	@Autowired
	public EditEventController(CalendarEventDao eventDao, EditEventValidator validator, MessageSource messageSource) {
		super(eventDao, validator, messageSource);
	}

	@RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
	public String initForm(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {

		EditEvent command = new EditEvent();

		CalendarEvent calendarEvent = this.eventDao.findById(eventId);
		command.setEvent(calendarEvent);

		command.setNewPriority(Localization.findLocaleMessage(this.messageSource, request, calendarEvent
				.getTypedPriority().getLocalizationCode()));

		model.addAttribute(EditEventController.COMMAND, command);
		return PageViews.EDIT.getViewName();
	}

	@RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL + "ulozeno/", method = RequestMethod.GET)
	public String initFormAfterSave(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {
		String view = this.initForm(eventId, request, model);
		((EditEvent) model.get(EditEventController.COMMAND)).setDataSaved(true);
		return view;
	}

	@RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
	public String processSubmittedForm(@ModelAttribute(EditEventController.COMMAND) EditEvent command,
			BindingResult result, SessionStatus status, @PathVariable int eventId, HttpServletRequest request,
			ModelMap model) {

		this.validator.validate(command, result);
		if (result.hasErrors()) {
			return PageViews.EDIT.getViewName();
		}

		// Updates the original data.
		CalendarEvent originalEvent = this.eventDao.findById(eventId);
		CalendarEvent newEvent = command.getEvent();
		originalEvent.setName(newEvent.getName());
		originalEvent.setDate(newEvent.getDate());
		originalEvent.setDescription(newEvent.getDescription());
		originalEvent.setPriority(PriorityType.valueOfAccordingLocalization(command.getNewPriority(),
				this.messageSource, request));

		try {
			// Updates the event in the repository.
			this.eventDao.update(originalEvent);

			// Clears the command object from the session.
			status.setComplete();

			// Returns the form success view.
			model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
					String.format("%sexistujici/%d/ulozeno/", EditEventController.BASE_URL, eventId));
			return Configuration.REDIRECTION_PAGE;

		} catch (DataAccessException e) {
			// We encountered a database problem.
			this.logger.error(originalEvent, e);
			result.reject(EditEventController.DATABASE_ERROR_MESSAGE_CODE);
		}
		return PageViews.EDIT.getViewName();
	}
}