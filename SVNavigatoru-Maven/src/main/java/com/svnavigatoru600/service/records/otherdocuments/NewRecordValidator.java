package com.svnavigatoru600.service.records.otherdocuments;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class NewRecordValidator extends OtherDocumentRecordValidator {

    public boolean supports(Class<?> clazz) {
        return NewRecord.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        NewRecord command = (NewRecord) target;
        this.checkNewName(command.getRecord().getName(), errors);
        this.checkNewFile(command.getNewFile(), errors);
        this.checkNewTypes(command.getNewTypes(), errors);
    }
}
