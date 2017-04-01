package com.svnavigatoru600.web.records.otherdocuments.revisions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.records.otherdocuments.RevisionsUrlParts;
import com.svnavigatoru600.viewmodel.records.otherdocuments.EditRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.EditRecordValidator;
import com.svnavigatoru600.web.SendNotificationEditModelFiller;
import com.svnavigatoru600.web.records.otherdocuments.AbstractEditDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditRevisionDocumentController extends AbstractEditDocumentController {

    /**
     * Constructor.
     */
    @Inject
    public EditRevisionDocumentController(final OtherDocumentRecordService recordService,
            final SendNotificationEditModelFiller sendNotificationModelFiller, final EditRecordValidator validator,
            final MessageSource messageSource) {
        super(RevisionsUrlParts.EXISTING_URL, new PageViews(), OtherDocumentRecordType.REGULAR_REVISION, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.EXISTING_URL + "{recordId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.EXISTING_URL + "{recordId}/"
            + CommonUrlParts.SAVED_EXTENSION, method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.EXISTING_URL + "{recordId}/", method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute(AbstractEditDocumentController.COMMAND) final EditRecord command,
            final BindingResult result, final SessionStatus status, @PathVariable final int recordId, final HttpServletRequest request,
            final ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
