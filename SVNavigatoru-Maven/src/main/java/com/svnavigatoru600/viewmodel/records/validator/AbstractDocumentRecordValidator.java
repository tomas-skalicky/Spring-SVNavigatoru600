package com.svnavigatoru600.viewmodel.records.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.svnavigatoru600.service.Configuration;
import com.svnavigatoru600.service.util.File;

/**
 * {@link Validator} common for all sub-types of
 * {@link com.svnavigatoru600.domain.records.AbstractDocumentRecord AbstractDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Service
public abstract class AbstractDocumentRecordValidator implements Validator {

    /**
     * Checks whether the given <code>file</code> is valid.
     */
    protected void checkNewFile(MultipartFile file, Errors errors) {
        String field = "newFile";

        int oldErrorCount = errors.getAllErrors().size();
        this.checkNewFileName(file.getOriginalFilename(), errors, field);
        int newErrorCount = errors.getAllErrors().size();
        if (newErrorCount != oldErrorCount) {
            // The filename is bad.
            return;
        }

        if (file.getSize() > Configuration.MAX_UPLOAD_SIZE) {
            errors.rejectValue(field, "file.is-too-large");
        }
    }

    /**
     * Checks whether the given <code>fileName</code> is valid. If not, associated the error with the given
     * <code>field</code>.
     */
    private void checkNewFileName(String fileName, Errors errors, String field) {
        if (StringUtils.isBlank(fileName)) {
            errors.rejectValue(field, "document-record.file-not-attached");
        } else {
            if (!File.isValid(fileName)) {
                errors.rejectValue(field, "document-record.supported-file-formats-are-and-no-other");
            }
        }
    }
}
