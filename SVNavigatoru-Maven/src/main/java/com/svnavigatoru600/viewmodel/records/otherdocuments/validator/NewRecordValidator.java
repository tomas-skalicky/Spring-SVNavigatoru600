package com.svnavigatoru600.viewmodel.records.otherdocuments.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.records.otherdocuments.NewRecord;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class NewRecordValidator extends AbstractOtherDocumentRecordValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NewRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewRecord command = (NewRecord) target;
        this.checkNewName(command.getRecord().getName(), errors);
        this.checkNewFile(command.getNewFile(), errors);
        this.checkNewTypes(command.getNewTypes(), errors);
    }
}