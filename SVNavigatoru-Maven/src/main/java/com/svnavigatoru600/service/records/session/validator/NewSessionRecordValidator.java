package com.svnavigatoru600.service.records.session.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;

@Service
public class NewSessionRecordValidator extends SessionRecordValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewSessionRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewSessionRecord command = (NewSessionRecord) target;
        this.checkNewType(command.getNewType(), errors);
        this.checkNewSessionDate(command.getRecord().getSessionDate(), errors);
        this.checkNewDiscussedTopics(command.getRecord().getDiscussedTopics(), errors);
        this.checkNewFile(command.getNewFile(), errors);
    }
}
