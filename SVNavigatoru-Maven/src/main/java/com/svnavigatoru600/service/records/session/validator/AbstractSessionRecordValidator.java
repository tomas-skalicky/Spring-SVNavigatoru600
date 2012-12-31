package com.svnavigatoru600.service.records.session.validator;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.records.SessionRecord;
import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.validator.AbstractDocumentRecordValidator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractSessionRecordValidator extends AbstractDocumentRecordValidator {

    protected void checkNewType(String newType, Errors errors) {
        try {
            SessionRecordType.valueOf(newType);
        } catch (IllegalArgumentException e) {
            // Cannot be correctly checked since the newType parameter contains
            // a localized string.
            ;
        } catch (NullPointerException e) {
            throw new RuntimeException("Name is null.", e);
        }
    }

    /**
     * Checks whether the given {@link SessionRecord}'s <code>sessionDate</code> is valid.
     */
    protected void checkNewSessionDate(Date sessionDate, Errors errors) {
        String field = "record.sessionDate";
        if (sessionDate == null) {
            errors.rejectValue(field, "session-records.session-date.not-filled-in");
        } else if (sessionDate.after(new Date())) {
            errors.rejectValue(field, "session-records.session-date.cannot-take-place-in-future");
        }
    }

    /**
     * Checks whether the given {@link SessionRecord}'s <code>discussedTopics</code> is valid.
     */
    protected void checkNewDiscussedTopics(String discussedTopics, Errors errors) {
        String field = "record.discussedTopics";
        if (StringUtils.isBlank(discussedTopics)) {
            errors.rejectValue(field, "session-records.discussed-topics.not-filled-in");
        }
    }
}
