package com.svnavigatoru600.viewmodel.records.session.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.records.session.EditSessionRecord;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditSessionRecordValidator extends AbstractSessionRecordValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditSessionRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditSessionRecord command = (EditSessionRecord) target;
        checkNewType(command.getNewType(), errors);
        checkNewSessionDate(command.getRecord().getSessionDate(), errors);
        checkNewDiscussedTopics(command.getRecord().getDiscussedTopics(), errors);
        if (command.isFileChanged()) {
            checkNewFile(command.getNewFile(), errors);
        }
    }
}
