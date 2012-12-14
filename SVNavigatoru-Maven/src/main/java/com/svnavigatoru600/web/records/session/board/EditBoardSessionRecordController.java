package com.svnavigatoru600.web.records.session.board;

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

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.records.session.EditSessionRecord;
import com.svnavigatoru600.service.records.session.EditSessionRecordValidator;
import com.svnavigatoru600.web.records.session.EditRecordController;

@Controller
public class EditBoardSessionRecordController extends EditRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/vybor/existujici/";

    /**
     * Constructor.
     */
    @Autowired
    public EditBoardSessionRecordController(SessionRecordDao recordDao, EditSessionRecordValidator validator,
            MessageSource messageSource) {
        super(EditBoardSessionRecordController.BASE_URL, new PageViews(),
                SessionRecordType.SESSION_RECORD_OF_BOARD, recordDao, validator, messageSource);
    }

    @Override
    @RequestMapping(value = EditBoardSessionRecordController.BASE_URL + "{recordId}/", method = RequestMethod.GET)
    public String initForm(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditBoardSessionRecordController.BASE_URL + "{recordId}/ulozeno/", method = RequestMethod.GET)
    public String initFormAfterSave(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @RequestMapping(value = EditBoardSessionRecordController.BASE_URL + "{recordId}/", method = RequestMethod.POST)
    public String processSubmittedForm(
            @ModelAttribute(EditRecordController.COMMAND) EditSessionRecord command, BindingResult result,
            SessionStatus status, @PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
