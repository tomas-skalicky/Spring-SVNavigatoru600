package svnavigatoru.service.records.otherdocuments;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import svnavigatoru.domain.records.OtherDocumentRecord;
import svnavigatoru.service.records.DocumentRecordValidator;
import svnavigatoru.service.util.OtherDocumentRecordUtils;

@Service
public abstract class OtherDocumentRecordValidator extends DocumentRecordValidator {

	/**
	 * Checks whether the given {@link OtherDocumentRecord}'s
	 * <code>recordName</code> is valid.
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
		for (boolean indicator : typeIndicators) {
			if (indicator == true) {
				// Everything OK since at least one type is checked.
				return;
			}
		}
		errors.rejectValue("newTypes", "other-documents.areas.at-least-one-must-be-checked");
	}
}
