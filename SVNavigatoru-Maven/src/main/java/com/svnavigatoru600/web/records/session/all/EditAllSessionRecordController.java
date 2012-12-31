package com.svnavigatoru600.web.records.session.all;

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

import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.records.session.validator.EditSessionRecordValidator;
import com.svnavigatoru600.viewmodel.records.session.EditSessionRecord;
import com.svnavigatoru600.web.records.session.AbstractEditRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditAllSessionRecordController extends AbstractEditRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/existujici/";

    /**
     * Constructor.
     */
    @Inject
    public EditAllSessionRecordController(SessionRecordDao recordDao, EditSessionRecordValidator validator,
            MessageSource messageSource) {
        super(EditAllSessionRecordController.BASE_URL, new PageViews(), recordDao, validator, messageSource);
    }

    @Override
    @RequestMapping(value = EditAllSessionRecordController.BASE_URL + "{recordId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditAllSessionRecordController.BASE_URL + "{recordId}/ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditAllSessionRecordController.BASE_URL + "{recordId}/", method = RequestMethod.POST)
    public String processSubmittedForm(
            @ModelAttribute(AbstractEditRecordController.COMMAND) EditSessionRecord command,
            BindingResult result, SessionStatus status, @PathVariable int recordId,
            HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
