package com.svnavigatoru600.service.eventcalendar.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;
import com.svnavigatoru600.viewmodel.eventcalendar.NewEvent;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewEventValidator extends AbstractEventValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewEvent.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewEvent command = (NewEvent) target;
        CalendarEvent event = command.getEvent();
        this.checkNewName(event.getName(), errors);
        this.checkNewDate(event.getDate(), errors);
        this.checkNewPriority(command.getNewPriority(), errors);
    }
}
