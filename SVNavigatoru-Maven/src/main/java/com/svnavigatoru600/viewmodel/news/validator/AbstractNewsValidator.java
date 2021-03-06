package com.svnavigatoru600.viewmodel.news.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractNewsValidator implements Validator {

    protected void checkNewTitle(String title, Errors errors) {
        String field = "news.title";
        if (StringUtils.isBlank(title)) {
            errors.rejectValue(field, "news.news-title.not-filled-in");
        }
    }

    protected void checkNewText(String text, Errors errors) {
        String field = "news.text";
        if (StringUtils.isBlank(text)) {
            errors.rejectValue(field, "news.text.not-filled-in");
        }
    }
}
