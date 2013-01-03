package com.svnavigatoru600.viewmodel.eventcalendar.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.viewmodel.eventcalendar.EditEvent;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditEventValidator extends AbstractEventValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditEvent.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditEvent command = (EditEvent) target;
        CalendarEvent event = command.getEvent();
        this.checkNewName(event.getName(), errors);
        this.checkNewDate(event.getDate(), errors);
        this.checkNewPriority(command.getNewPriority(), errors);
    }
}
