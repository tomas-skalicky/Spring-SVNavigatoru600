package com.svnavigatoru600.web.eventcalendar;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.eventcalendar.EditEvent;
import com.svnavigatoru600.viewmodel.eventcalendar.validator.EditEventValidator;
import com.svnavigatoru600.web.AbstractMetaController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditEventController extends AbstractNewEditEventController {

    private static final String REQUEST_MAPPING_BASE_URL = EditEventController.BASE_URL
            + "existujici/{eventId}/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    /**
     * Constructor.
     */
    @Inject
    public EditEventController(CalendarEventService eventService, EditEventValidator validator,
            MessageSource messageSource) {
        super(eventService, validator, messageSource);
    }

    @RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {

        EditEvent command = new EditEvent();

        CalendarEvent calendarEvent = this.getEventService().findById(eventId);
        command.setEvent(calendarEvent);

        command.setNewPriority(Localization.findLocaleMessage(this.getMessageSource(), request, calendarEvent
                .getTypedPriority().getLocalizationCode()));

        model.addAttribute(AbstractNewEditEventController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL + "ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {
        String view = this.initForm(eventId, request, model);
        ((EditEvent) model.get(AbstractNewEditEventController.COMMAND)).setDataSaved(true);
        return view;
    }

    @RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditEventController.COMMAND) EditEvent command,
            BindingResult result, SessionStatus status, @PathVariable int eventId,
            HttpServletRequest request, ModelMap model) {

        this.getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        CalendarEventService eventService = this.getEventService();
        CalendarEvent originalEvent = null;
        try {
            originalEvent = eventService.findById(eventId);
            PriorityType newPriority = PriorityType.valueOfAccordingLocalization(command.getNewPriority(),
                    this.getMessageSource(), request);
            eventService.update(originalEvent, command.getEvent(), newPriority);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%sexistujici/%d/ulozeno/", AbstractEventController.BASE_URL, eventId));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalEvent, e);
            result.reject(EditEventController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
