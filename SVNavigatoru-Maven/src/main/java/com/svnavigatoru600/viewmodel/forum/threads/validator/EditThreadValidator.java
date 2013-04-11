package com.svnavigatoru600.viewmodel.forum.threads.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.forum.threads.EditThread;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditThreadValidator extends AbstractThreadValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditThread.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditThread command = (EditThread) target;
        checkNewName(command.getThread().getName(), errors);
    }
}
