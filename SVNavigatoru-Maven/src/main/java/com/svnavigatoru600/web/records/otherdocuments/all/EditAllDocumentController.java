package com.svnavigatoru600.web.records.otherdocuments.all;

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

import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.validator.EditRecordValidator;
import com.svnavigatoru600.viewmodel.records.otherdocuments.EditRecord;
import com.svnavigatoru600.web.records.otherdocuments.AbstractEditDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditAllDocumentController extends AbstractEditDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/existujici/";

    /**
     * Constructor.
     */
    @Inject
    public EditAllDocumentController(OtherDocumentRecordDao recordDao, EditRecordValidator validator,
            MessageSource messageSource) {
        super(EditAllDocumentController.BASE_URL, new PageViews(), recordDao, validator, messageSource);
    }

    @Override
    @RequestMapping(value = EditAllDocumentController.BASE_URL + "{recordId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditAllDocumentController.BASE_URL + "{recordId}/ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditAllDocumentController.BASE_URL + "{recordId}/", method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute(AbstractEditDocumentController.COMMAND) EditRecord command,
            BindingResult result, SessionStatus status, @PathVariable int recordId,
            HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
