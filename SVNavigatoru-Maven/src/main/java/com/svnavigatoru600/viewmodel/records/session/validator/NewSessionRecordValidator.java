package com.svnavigatoru600.viewmodel.records.session.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewSessionRecordValidator extends AbstractSessionRecordValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewSessionRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewSessionRecord command = (NewSessionRecord) target;
        checkNewType(command.getNewType(), errors);
        checkNewSessionDate(command.getRecord().getSessionDate(), errors);
        checkNewDiscussedTopics(command.getRecord().getDiscussedTopics(), errors);
        checkNewFile(command.getNewFile(), errors);
    }
}
