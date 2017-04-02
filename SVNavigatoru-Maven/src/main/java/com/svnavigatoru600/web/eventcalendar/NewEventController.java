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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityTypeEnum;
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

    @Inject
    public NewEventController(final CalendarEventService eventService,
            final SendNotificationNewModelFiller sendNotificationModelFiller, final NewEventValidator validator,
            final MessageSource messageSource) {
        super(eventService, sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @GetMapping(value = EventsUrlParts.NEW_URL)
    public String initForm(final HttpServletRequest request, final ModelMap model) {

        final NewEvent command = new NewEvent();

        final CalendarEvent calendarEvent = new CalendarEvent();
        command.setEvent(calendarEvent);

        final MessageSource messageSource = getMessageSource();
        command.setNewPriority(
                Localization.findLocaleMessage(messageSource, request, PriorityTypeEnum.NORMAL.getLocalizationCode()));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditEventController.COMMAND, command);
        return PageViewsEnum.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new calendar event is stored to the repository. Otherwise, returns back to the
     * form.
     *
     * @return The name of the view which is to be shown.
     */
    @PostMapping(value = EventsUrlParts.NEW_URL)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(NewEventController.COMMAND) final NewEvent command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {

        final MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViewsEnum.NEW.getViewName();
        }

        // Updates the data of the new calendar event.
        final CalendarEvent newEvent = command.getEvent();
        newEvent.setPriority(
                PriorityTypeEnum.valueOfAccordingLocalization(command.getNewPriority(), messageSource, request));

        try {
            // Saves the event to the repository.
            getEventService().saveAndNotifyUsers(newEvent, command.getSendNotification().isStatus(), request,
                    messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, EventsUrlParts.CREATED_URL);
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(newEvent, e);
            result.reject(NewEventController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViewsEnum.NEW.getViewName();
    }
}
