package com.svnavigatoru600.service.eventcalendar;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.eventcalendar.CalendarEvent;

@Service
public class NewEventValidator extends EventValidator {

    public boolean supports(Class<?> clazz) {
        return NewEvent.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        NewEvent command = (NewEvent) target;
        CalendarEvent event = command.getEvent();
        this.checkNewName(event.getName(), errors);
        this.checkNewDate(event.getDate(), errors);
        this.checkNewPriority(command.getNewPriority(), errors);
    }
}
