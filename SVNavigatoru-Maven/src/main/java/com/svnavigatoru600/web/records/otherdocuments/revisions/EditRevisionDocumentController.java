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
import com.svnavigatoru600.viewmodel.records.otherdocuments.EditRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.EditRecordValidator;
import com.svnavigatoru600.web.records.otherdocuments.AbstractEditDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditRevisionDocumentController extends AbstractEditDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/pravidelne-revize/existujici/";

    /**
     * Constructor.
     */
    @Inject
    public EditRevisionDocumentController(OtherDocumentRecordService recordService,
            EditRecordValidator validator, MessageSource messageSource) {
        super(EditRevisionDocumentController.BASE_URL, new PageViews(),
                OtherDocumentRecordType.REGULAR_REVISION, recordService, validator, messageSource);
    }

    @Override
    @RequestMapping(value = EditRevisionDocumentController.BASE_URL + "{recordId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditRevisionDocumentController.BASE_URL + "{recordId}/ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditRevisionDocumentController.BASE_URL + "{recordId}/", method = RequestMethod.POST)
    public String processSubmittedForm(
            @ModelAttribute(AbstractEditDocumentController.COMMAND) EditRecord command, BindingResult result,
            SessionStatus status, @PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
