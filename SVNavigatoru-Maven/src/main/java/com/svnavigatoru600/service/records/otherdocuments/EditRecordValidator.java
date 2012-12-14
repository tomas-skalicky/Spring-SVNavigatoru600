package com.svnavigatoru600.service.records.otherdocuments;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class EditRecordValidator extends OtherDocumentRecordValidator {

    public boolean supports(Class<?> clazz) {
        return EditRecord.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        EditRecord command = (EditRecord) target;
        this.checkNewName(command.getRecord().getName(), errors);
        if (command.isFileChanged()) {
            this.checkNewFile(command.getNewFile(), errors);
        }
        this.checkNewTypes(command.getNewTypes(), errors);
    }
}
