package com.svnavigatoru600.service.records.session.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.records.session.EditSessionRecord;

@Service
public class EditSessionRecordValidator extends SessionRecordValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditSessionRecord.class.isAssignableFrom(clazz);
    }

    @Override
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
