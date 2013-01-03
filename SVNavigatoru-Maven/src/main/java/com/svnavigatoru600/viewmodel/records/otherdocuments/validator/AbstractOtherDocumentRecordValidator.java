package com.svnavigatoru600.viewmodel.records.otherdocuments.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.service.util.OtherDocumentRecordUtils;
import com.svnavigatoru600.viewmodel.records.validator.AbstractDocumentRecordValidator;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractOtherDocumentRecordValidator extends AbstractDocumentRecordValidator {

    /**
     * Checks whether the given {@link OtherDocumentRecord OtherDocumentRecord's} <code>recordName</code> is
     * valid.
     */
    protected void checkNewName(String recordName, Errors errors) {
        String field = "record.name";
        if (StringUtils.isBlank(recordName)) {
            errors.rejectValue(field, "other-documents.document-name.not-filled-in");
        } else {
            if (!OtherDocumentRecordUtils.isRecordNameValid(recordName)) {
                errors.rejectValue(field, "other-documents.document-name.bad-format");
            }
        }
    }

    protected void checkNewTypes(boolean[] typeIndicators, Errors errors) {
        for (boolean isTypeChecked : typeIndicators) {
            if (isTypeChecked) {
                // Everything OK since at least one type is checked.
                return;
            }
        }
        errors.rejectValue("newTypes", "other-documents.areas.at-least-one-must-be-checked");
    }
}
