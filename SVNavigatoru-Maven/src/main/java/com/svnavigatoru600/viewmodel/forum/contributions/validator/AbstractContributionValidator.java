package com.svnavigatoru600.viewmodel.forum.contributions.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractContributionValidator implements Validator {

    public void checkNewText(String text, Errors errors) {
        String field = "contribution.text";
        if (StringUtils.isBlank(text)) {
            errors.rejectValue(field, "forum.contributions.text.not-filled-in");
        }
    }
}
