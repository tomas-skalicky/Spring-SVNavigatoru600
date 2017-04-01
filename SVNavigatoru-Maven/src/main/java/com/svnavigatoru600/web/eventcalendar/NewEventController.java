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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.url.eventcalendar.EventsUrlParts;
import com.svnavigatoru600.viewmodel.eventcalendar.NewEvent;
import com.svnavigatoru600.viewmodel.eventcalendar.validator.NewEventValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewEventController extends AbstractNewEditEventController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "event-calendar.adding-failed-due-to-database-error";

    /**
     * Constructor.
     */
    @Inject
    public NewEventController(CalendarEventService eventService,
            SendNotificationNewModelFiller sendNotificationModelFiller, NewEventValidator validator,
            MessageSource messageSource) {
        super(eventService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = EventsUrlParts.NEW_URL, method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {

        NewEvent command = new NewEvent();

        CalendarEvent calendarEvent = new CalendarEvent();
        command.setEvent(calendarEvent);

        MessageSource messageSource = getMessageSource();
        command.setNewPriority(
                Localization.findLocaleMessage(messageSource, request, PriorityType.NORMAL.getLocalizationCode()));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditEventController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new calendar event is stored to the repository. Otherwise, returns back to the
     * form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = EventsUrlParts.NEW_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(NewEventController.COMMAND) NewEvent command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {

        MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        // Updates the data of the new calendar event.
        CalendarEvent newEvent = command.getEvent();
        newEvent.setPriority(
                PriorityType.valueOfAccordingLocalization(command.getNewPriority(), messageSource, request));

        try {
            // Saves the event to the repository.
            getEventService().saveAndNotifyUsers(newEvent, command.getSendNotification().isStatus(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, EventsUrlParts.CREATED_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newEvent, e);
            result.reject(NewEventController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.NEW.getViewName();
    }
}
