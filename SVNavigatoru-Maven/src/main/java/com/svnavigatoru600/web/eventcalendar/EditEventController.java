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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityTypeEnum;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.eventcalendar.EventsUrlParts;
import com.svnavigatoru600.viewmodel.eventcalendar.EditEvent;
import com.svnavigatoru600.viewmodel.eventcalendar.validator.EditEventValidator;
import com.svnavigatoru600.web.AbstractMetaController;
import com.svnavigatoru600.web.SendNotificationEditModelFiller;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditEventController extends AbstractNewEditEventController {

    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "edit.changes-not-saved-due-to-database-error";

    @Inject
    public EditEventController(final CalendarEventService eventService,
            final SendNotificationEditModelFiller sendNotificationModelFiller, final EditEventValidator validator,
            final MessageSource messageSource) {
        super(eventService, sendNotificationModelFiller, validator, messageSource);
    }

    @GetMapping(value = EventsUrlParts.EXISTING_URL + "{eventId}/")
    public String initForm(@PathVariable final int eventId, final HttpServletRequest request, final ModelMap model) {

        final EditEvent command = new EditEvent();

        final CalendarEvent calendarEvent = getEventService().findById(eventId);
        command.setEvent(calendarEvent);

        final MessageSource messageSource = getMessageSource();
        command.setNewPriority(Localization.findLocaleMessage(messageSource, request,
                calendarEvent.getTypedPriority().getLocalizationCode()));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditEventController.COMMAND, command);
        return PageViewsEnum.EDIT.getViewName();
    }

    @GetMapping(value = EventsUrlParts.EXISTING_URL + "{eventId}/" + CommonUrlParts.SAVED_EXTENSION)
    public String initFormAfterSave(@PathVariable final int eventId, final HttpServletRequest request,
            final ModelMap model) {
        final String view = initForm(eventId, request, model);
        ((EditEvent) model.get(AbstractNewEditEventController.COMMAND)).setDataSaved(true);
        return view;
    }

    @PostMapping(value = EventsUrlParts.EXISTING_URL + "{eventId}/")
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditEventController.COMMAND) final EditEvent command,
            final BindingResult result, final SessionStatus status, @PathVariable final int eventId,
            final HttpServletRequest request, final ModelMap model) {

        final MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller().populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViewsEnum.EDIT.getViewName();
        }

        final CalendarEventService eventService = getEventService();
        CalendarEvent originalEvent = null;
        try {
            originalEvent = eventService.findById(eventId);
            final PriorityTypeEnum newPriority = PriorityTypeEnum.valueOfAccordingLocalization(command.getNewPriority(),
                    messageSource, request);
            eventService.updateAndNotifyUsers(originalEvent, command.getEvent(), newPriority,
                    command.getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE,
                    String.format("%s%d/%s", EventsUrlParts.EXISTING_URL, eventId, CommonUrlParts.SAVED_EXTENSION));
            // Does not work. I do not know why.
            // model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format(
            // "%s%d/%s?%s=%b", EventsUrlParts.EXISTING_URL, eventId, CommonUrlParts.SAVED_EXTENSION,
            // SEND_NOTIFICATION_GET_PARAMETER, command.getSendNotification()));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (final DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalEvent, e);
            result.reject(EditEventController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViewsEnum.EDIT.getViewName();
    }
}
