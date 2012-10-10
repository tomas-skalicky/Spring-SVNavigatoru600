package svnavigatoru.service.eventcalendar;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import svnavigatoru.domain.eventcalendar.CalendarEvent;

@Service
public class EditEventValidator extends EventValidator {

	public boolean supports(Class<?> clazz) {
		return EditEvent.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		EditEvent command = (EditEvent) target;
		CalendarEvent event = command.getEvent();
		this.checkNewName(event.getName(), errors);
		this.checkNewDate(event.getDate(), errors);
		this.checkNewPriority(command.getNewPriority(), errors);
	}
}
