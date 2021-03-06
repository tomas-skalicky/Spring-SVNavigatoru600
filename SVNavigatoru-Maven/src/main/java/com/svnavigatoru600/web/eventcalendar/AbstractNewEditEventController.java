package com.svnavigatoru600.web.eventcalendar;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.google.common.collect.Lists;
import com.svnavigatoru600.domain.eventcalendar.PriorityTypeEnum;
import com.svnavigatoru600.service.eventcalendar.CalendarEventService;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.viewmodel.eventcalendar.validator.AbstractEventValidator;
import com.svnavigatoru600.web.SendNotificationController;
import com.svnavigatoru600.web.SendNotificationModelFiller;

/**
 * Parent of controllers which create and edit the {@link com.svnavigatoru600.domain.eventcalendar.CalendarEvent
 * CalendarEvents}.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public abstract class AbstractNewEditEventController extends AbstractEventController
        implements SendNotificationController {

    /**
     * Command used in /main-content/event-calendar/new-edit-event.jsp.
     */
    public static final String COMMAND = "newEditEventCommand";
    private final Validator validator;
    private final SendNotificationModelFiller sendNotificationModelFiller;

    public AbstractNewEditEventController(final CalendarEventService eventService,
            final SendNotificationModelFiller sendNotificationModelFiller, final AbstractEventValidator validator,
            final MessageSource messageSource) {
        super(eventService, messageSource);
        this.validator = validator;
        this.sendNotificationModelFiller = sendNotificationModelFiller;
    }

    /**
     * Trivial getter
     */
    protected Validator getValidator() {
        return validator;
    }

    /**
     * Trivial getter
     */
    protected SendNotificationModelFiller getSendNotificationModelFiller() {
        return sendNotificationModelFiller;
    }

    /**
     * Creates a {@link List} of localized names of {@link PriorityTypeEnum PriorityTypes}. The forms which use this
     * controller can access the resulting list.
     * <p>
     * This method is used for filling up the tag <em>radiobuttons</em> and the value of the selected radiobutton is
     * stored to <code>NewEditEvent.newPriority</code>.
     */
    @ModelAttribute("priorityTypeList")
    public List<String> populatePriorityTypeList(final HttpServletRequest request) {
        final List<String> priorityTypeList = Lists.newArrayList();

        for (final PriorityTypeEnum type : PriorityTypeEnum.values()) {
            final String localizationCode = type.getLocalizationCode();
            priorityTypeList.add(Localization.findLocaleMessage(getMessageSource(), request, localizationCode));
        }
        return priorityTypeList;
    }
}
