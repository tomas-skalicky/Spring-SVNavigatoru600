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

    /**
     * Constructor.
     */
    @Inject
    public EditEventController(CalendarEventService eventService,
            SendNotificationEditModelFiller sendNotificationModelFiller, EditEventValidator validator,
            MessageSource messageSource) {
        super(eventService, sendNotificationModelFiller, validator, messageSource);
    }

    @RequestMapping(value = EventsUrlParts.EXISTING_URL + "{eventId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {

        EditEvent command = new EditEvent();

        CalendarEvent calendarEvent = getEventService().findById(eventId);
        command.setEvent(calendarEvent);

        MessageSource messageSource = getMessageSource();
        command.setNewPriority(Localization.findLocaleMessage(messageSource, request, calendarEvent
                .getTypedPriority().getLocalizationCode()));

        getSendNotificationModelFiller().populateSendNotificationInInitForm(command, request, messageSource);

        model.addAttribute(AbstractNewEditEventController.COMMAND, command);
        return PageViews.EDIT.getViewName();
    }

    @RequestMapping(value = EventsUrlParts.EXISTING_URL + "{eventId}/" + CommonUrlParts.SAVED_EXTENSION, method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int eventId, HttpServletRequest request, ModelMap model) {
        String view = initForm(eventId, request, model);
        ((EditEvent) model.get(AbstractNewEditEventController.COMMAND)).setDataSaved(true);
        return view;
    }

    // Does not work. I do not know why.
    /**
     * see http://stackoverflow.com/questions/13213061/springmvc-requestmapping-for-get-parameters
     */
    // @RequestMapping(value = EditEventController.REQUEST_MAPPING_BASE_URL + CommonUrlParts.SAVED_EXTENSION,
    // params = { "eventId", "sendNotification" }, method = RequestMethod.GET)
    // public String initFormAfterSave(@PathVariable("eventId") int eventId,
    // @PathVariable("sendNotification") boolean sendNotification, HttpServletRequest request,
    // ModelMap model) {
    // String view = this.initForm(eventId, request, model);
    // EditEvent command = (EditEvent) model.get(AbstractNewEditEventController.COMMAND);
    // command.setDataSaved(true);
    // command.getSendNotification().setStatus(sendNotification);
    // return view;
    // }

    @RequestMapping(value = EventsUrlParts.EXISTING_URL + "{eventId}/", method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(EditEventController.COMMAND) EditEvent command,
            BindingResult result, SessionStatus status, @PathVariable int eventId,
            HttpServletRequest request, ModelMap model) {

        MessageSource messageSource = getMessageSource();
        getSendNotificationModelFiller()
                .populateSendNotificationInSubmitForm(command, request, messageSource);

        getValidator().validate(command, result);
        if (result.hasErrors()) {
            return PageViews.EDIT.getViewName();
        }

        CalendarEventService eventService = getEventService();
        CalendarEvent originalEvent = null;
        try {
            originalEvent = eventService.findById(eventId);
            PriorityType newPriority = PriorityType.valueOfAccordingLocalization(command.getNewPriority(),
                    messageSource, request);
            eventService.updateAndNotifyUsers(originalEvent, command.getEvent(), newPriority, command
                    .getSendNotification().isStatus(), request, messageSource);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format("%s%d/%s",
                    EventsUrlParts.EXISTING_URL, eventId, CommonUrlParts.SAVED_EXTENSION));
            // Does not work. I do not know why.
            // model.addAttribute(AbstractMetaController.REDIRECTION_ATTRIBUTE, String.format(
            // "%s%d/%s?%s=%b", EventsUrlParts.EXISTING_URL, eventId, CommonUrlParts.SAVED_EXTENSION,
            // SEND_NOTIFICATION_GET_PARAMETER, command.getSendNotification()));
            return AbstractMetaController.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            LogFactory.getLog(this.getClass()).error(originalEvent, e);
            result.reject(EditEventController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.EDIT.getViewName();
    }
}
