package com.svnavigatoru600.service.forum.threads;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditThreadValidator extends ThreadValidator {

	public boolean supports(Class<?> clazz) {
		return EditThread.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		EditThread command = (EditThread) target;
		this.checkNewName(command.getThread().getName(), errors);
	}
}
