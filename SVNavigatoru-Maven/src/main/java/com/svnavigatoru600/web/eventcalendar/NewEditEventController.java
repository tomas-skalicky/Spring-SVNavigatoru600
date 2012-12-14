package com.svnavigatoru600.web.eventcalendar;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.eventcalendar.PriorityType;
import com.svnavigatoru600.repository.CalendarEventDao;
import com.svnavigatoru600.service.eventcalendar.EventValidator;
import com.svnavigatoru600.service.util.Localization;

/**
 * Parent of controllers which create and edit the {@link CalendarEvent}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class NewEditEventController extends EventController {

    /**
     * Command used in /main-content/event-calendar/new-edit-event.jsp.
     */
    public static final String COMMAND = "newEditEventCommand";
    protected Validator validator;

    public NewEditEventController(CalendarEventDao eventDao, EventValidator validator,
            MessageSource messageSource) {
        super(eventDao, messageSource);
        this.validator = validator;
    }

    /**
     * <p>
     * Creates a {@list List} of localized names of {@link PriorityType}s. The forms which use this controller
     * can access the resulting list.
     * </p>
     * <p>
     * This method is used for filling up the tag <em>radiobuttons</em> and the value of the selected
     * radiobutton is stored to <code>NewEditEvent.newPriority</code>.
     * </p>
     */
    @ModelAttribute("priorityTypeList")
    public List<String> populatePriorityTypeList(HttpServletRequest request) {
        List<String> priorityTypeList = new ArrayList<String>();

        for (PriorityType type : PriorityType.values()) {
            String localizationCode = type.getLocalizationCode();
            priorityTypeList.add(Localization
                    .findLocaleMessage(this.messageSource, request, localizationCode));
        }
        return priorityTypeList;
    }
}
