package com.svnavigatoru600.web.records.otherdocuments.remostav;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.EditRecord;
import com.svnavigatoru600.service.records.otherdocuments.validator.EditRecordValidator;
import com.svnavigatoru600.web.records.otherdocuments.EditDocumentController;

@Controller
public class EditRemostavDocumentController extends EditDocumentController {

    private static final String BASE_URL = "/remostav/dokumentace/existujici/";

    /**
     * Constructor.
     */
    @Autowired
    public EditRemostavDocumentController(OtherDocumentRecordDao recordDao, EditRecordValidator validator,
            MessageSource messageSource) {
        super(EditRemostavDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.REMOSTAV,
                recordDao, validator, messageSource);
    }

    @Override
    @RequestMapping(value = EditRemostavDocumentController.BASE_URL + "{recordId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditRemostavDocumentController.BASE_URL + "{recordId}/ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditRemostavDocumentController.BASE_URL + "{recordId}/", method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute(EditDocumentController.COMMAND) EditRecord command,
            BindingResult result, SessionStatus status, @PathVariable int recordId,
            HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
