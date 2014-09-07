package com.svnavigatoru600.web.eventcalendar;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.eventcalendar.validator.AbstractEventValidator;
import com.svnavigatoru600.web.SendNotificationController;
import com.svnavigatoru600.web.SendNotificationModelFiller;

/**
 * Parent of controllers which create and edit the
 * {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent CalendarEvents}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractNewEditEventController extends AbstractEventController implements
        SendNotificationController {

    /**
     * Command used in /main-content/event-calendar/new-edit-event.jsp.
     */
    public static final String COMMAND = "newEditEventCommand";
    private final Validator validator;
    private final SendNotificationModelFiller sendNotificationModelFiller;

    public AbstractNewEditEventController(CalendarEventService eventService,
            SendNotificationModelFiller sendNotificationModelFiller, AbstractEventValidator validator,
            MessageSource messageSource) {
        super(eventService, messageSource);
        this.validator = validator;
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return this.validator;
    }

    /**
     * Trivial getter
     */
    protected SendNotificationModelFiller getSendNotificationModelFiller() {
        return this.sendNotificationModelFiller;
    }

    /**
     * Creates a {@list List} of localized names of {@link PriorityType PriorityTypes}. The forms which use
     * this controller can access the resulting list.
     * <p>
     * This method is used for filling up the tag <em>radiobuttons</em> and the value of the selected
     * radiobutton is stored to <code>NewEditEvent.newPriority</code>.
     */
    @ModelAttribute("priorityTypeList")
    public List<String> populatePriorityTypeList(HttpServletRequest request) {
        List<String> priorityTypeList = new ArrayList<String>();

        for (PriorityType type : PriorityType.values()) {
            String localizationCode = type.getLocalizationCode();
            priorityTypeList.add(Localization
                    .findLocaleMessage(getMessageSource(), request, localizationCode));
        }
        return priorityTypeList;
    }
}
