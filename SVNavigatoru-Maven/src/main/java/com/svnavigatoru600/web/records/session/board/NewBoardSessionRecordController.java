package com.svnavigatoru600.web.records.session.board;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.service.records.session.NewSessionRecord;
import com.svnavigatoru600.service.records.session.validator.NewSessionRecordValidator;
import com.svnavigatoru600.web.records.session.NewRecordController;

@Controller
public class NewBoardSessionRecordController extends NewRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/vybor/";

    /**
     * Constructor.
     */
    @Autowired
    public NewBoardSessionRecordController(SessionRecordDao recordDao, NewSessionRecordValidator validator,
            MessageSource messageSource) {
        super(NewBoardSessionRecordController.BASE_URL, new PageViews(),
                SessionRecordType.SESSION_RECORD_OF_BOARD, recordDao, validator, messageSource);
    }

    /**
     * This method cannot be annotated with {@link Override} since it has one less parameter.
     */
    @RequestMapping(value = NewBoardSessionRecordController.BASE_URL + "novy/", method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {
        return super.initForm(SessionRecordType.SESSION_RECORD_OF_BOARD, request, model);
    }

    @Override
    @RequestMapping(value = NewBoardSessionRecordController.BASE_URL + "novy/", method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute(NewRecordController.COMMAND) NewSessionRecord command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
