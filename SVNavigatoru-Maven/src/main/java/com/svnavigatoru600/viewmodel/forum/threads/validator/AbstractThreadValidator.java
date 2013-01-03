package com.svnavigatoru600.viewmodel.forum.threads.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractThreadValidator implements Validator {

    protected void checkNewName(String name, Errors errors) {
        String field = "thread.name";
        if (StringUtils.isBlank(name)) {
            errors.rejectValue(field, "forum.threads.name.not-filled-in");
        }
    }
}
