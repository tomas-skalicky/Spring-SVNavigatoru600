package com.svnavigatoru600.service.records.session;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditSessionRecordValidator extends SessionRecordValidator {

    public boolean supports(Class<?> clazz) {
        return EditSessionRecord.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        EditSessionRecord command = (EditSessionRecord) target;
        this.checkNewType(command.getNewType(), errors);
        this.checkNewSessionDate(command.getRecord().getSessionDate(), errors);
        this.checkNewDiscussedTopics(command.getRecord().getDiscussedTopics(), errors);
        if (command.isFileChanged()) {
            this.checkNewFile(command.getNewFile(), errors);
        }
    }
}
