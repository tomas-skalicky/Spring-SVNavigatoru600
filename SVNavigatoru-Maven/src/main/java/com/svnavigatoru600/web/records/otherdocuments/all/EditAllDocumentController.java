package com.svnavigatoru600.web.records.otherdocuments.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.viewmodel.records.otherdocuments.EditRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.EditRecordValidator;
import com.svnavigatoru600.web.SendNotificationEditModelFiller;
import com.svnavigatoru600.web.records.otherdocuments.AbstractEditDocumentController;
import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.records.otherdocuments.AllDocumentsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditAllDocumentController extends AbstractEditDocumentController {

    @Inject
    public EditAllDocumentController(final OtherDocumentRecordService recordService,
            final SendNotificationEditModelFiller sendNotificationModelFiller, final EditRecordValidator validator,
            final MessageSource messageSource) {
        super(AllDocumentsUrlParts.EXISTING_URL, new PageViews(), recordService, sendNotificationModelFiller, validator,
                messageSource);
    }

    @Override
    @GetMapping(value = AllDocumentsUrlParts.EXISTING_URL + "{recordId}/")
    public String initForm(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @GetMapping(value = AllDocumentsUrlParts.EXISTING_URL + "{recordId}/" + CommonUrlParts.SAVED_EXTENSION)
    public String initFormAfterSave(@PathVariable final int recordId, final HttpServletRequest request,
            final ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @PostMapping(value = AllDocumentsUrlParts.EXISTING_URL + "{recordId}/")
    public String processSubmittedForm(@ModelAttribute(AbstractEditDocumentController.COMMAND) final EditRecord command,
            final BindingResult result, final SessionStatus status, @PathVariable final int recordId,
            final HttpServletRequest request, final ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
