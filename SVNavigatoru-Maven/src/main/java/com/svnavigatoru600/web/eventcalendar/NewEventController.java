package com.svnavigatoru600.web.eventcalendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.eventcalendar.NewEvent;
import com.svnavigatoru600.service.eventcalendar.validator.NewEventValidator;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.Configuration;

@Controller
public class NewEventController extends NewEditEventController {

    private static final String REQUEST_MAPPING_BASE_URL = NewEventController.BASE_URL + "novy/";
    /**
     * Code of the error message used when the {@link DataAccessException} is thrown.
     */
    public static final String DATABASE_ERROR_MESSAGE_CODE = "event-calendar.adding-failed-due-to-database-error";

    /**
     * Constructor.
     */
    @Autowired
    public NewEventController(CalendarEventDao calendarEventDao, NewEventValidator validator,
            MessageSource messageSource) {
        super(calendarEventDao, validator, messageSource);
    }

    /**
     * Initializes the form.
     */
    @RequestMapping(value = NewEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {

        NewEvent command = new NewEvent();

        CalendarEvent calendarEvent = new CalendarEvent();
        command.setEvent(calendarEvent);

        command.setNewPriority(Localization.findLocaleMessage(this.messageSource, request,
                PriorityType.NORMAL.getLocalizationCode()));

        model.addAttribute(NewEventController.COMMAND, command);
        return PageViews.NEW.getViewName();
    }

    /**
     * If values in the form are OK, the new calendar event is stored to the repository. Otherwise, returns
     * back to the form.
     * 
     * @return The name of the view which is to be shown.
     */
    @RequestMapping(value = NewEventController.REQUEST_MAPPING_BASE_URL, method = RequestMethod.POST)
    @Transactional
    public String processSubmittedForm(@ModelAttribute(NewEventController.COMMAND) NewEvent command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {

        this.validator.validate(command, result);
        if (result.hasErrors()) {
            return PageViews.NEW.getViewName();
        }

        // Updates the data of the new calendar event.
        CalendarEvent newEvent = command.getEvent();
        newEvent.setPriority(PriorityType.valueOfAccordingLocalization(command.getNewPriority(),
                this.messageSource, request));

        try {
            // Saves the event to the repository.
            this.eventDao.save(newEvent);

            // Clears the command object from the session.
            status.setComplete();

            // Returns the form success view.
            model.addAttribute(Configuration.REDIRECTION_ATTRIBUTE,
                    String.format("%svytvoreno/", NewEventController.BASE_URL));
            return Configuration.REDIRECTION_PAGE;

        } catch (DataAccessException e) {
            // We encountered a database problem.
            this.logger.error(newEvent, e);
            result.reject(NewEventController.DATABASE_ERROR_MESSAGE_CODE);
        }
        return PageViews.NEW.getViewName();
    }
}
