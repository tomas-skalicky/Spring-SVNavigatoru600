package com.svnavigatoru600.viewmodel.eventcalendar.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.util.DateUtils;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractEventValidator implements Validator {

    protected void checkNewName(String name, Errors errors) {
        String field = "event.name";
        if (StringUtils.isBlank(name)) {
            errors.rejectValue(field, "event-calendar.name.not-filled-in");
        }
    }

    /**
     * Checks whether the given {@link CalendarEvent CalendarEvent's} <code>date</code> is valid.
     */
    protected void checkNewDate(Date newDate, Errors errors) {
        String field = "event.date";
        if (newDate == null) {
            errors.rejectValue(field, "event-calendar.date.not-filled-in");
        } else if (newDate.before(DateUtils.getToday())) {
            errors.rejectValue(field, "event-calendar.date.cannot-take-place-in-past");
        }
    }

    protected void checkNewPriority(String newPriority, Errors errors) {
        try {
            SessionRecordType.valueOf(newPriority);
        } catch (IllegalArgumentException e) {
            // Cannot be correctly checked since the newPriority parameter
            // contains a localized string.
            ;
        } catch (NullPointerException e) {
            throw new RuntimeException("Priority is null.", e);
        }
    }
}
