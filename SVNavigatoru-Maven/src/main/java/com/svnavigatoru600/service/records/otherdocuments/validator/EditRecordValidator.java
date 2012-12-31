package com.svnavigatoru600.service.records.otherdocuments.validator;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.svnavigatoru600.viewmodel.records.otherdocuments.EditRecord;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public class EditRecordValidator extends AbstractOtherDocumentRecordValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EditRecord.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EditRecord command = (EditRecord) target;
        this.checkNewName(command.getRecord().getName(), errors);
        if (command.isFileChanged()) {
            this.checkNewFile(command.getNewFile(), errors);
        }
        this.checkNewTypes(command.getNewTypes(), errors);
    }
}
