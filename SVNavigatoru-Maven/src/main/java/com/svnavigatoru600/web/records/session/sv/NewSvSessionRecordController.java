package com.svnavigatoru600.web.records.session.sv;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.records.session.SvUrlParts;
import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;
import com.svnavigatoru600.viewmodel.records.session.validator.NewSessionRecordValidator;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;
import com.svnavigatoru600.web.records.session.AbstractNewRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewSvSessionRecordController extends AbstractNewRecordController {

    /**
     * Constructor.
     */
    @Inject
    public NewSvSessionRecordController(SessionRecordService recordService,
            SendNotificationNewModelFiller sendNotificationModelFiller, NewSessionRecordValidator validator,
            MessageSource messageSource) {
        super(SvUrlParts.BASE_URL, new PageViews(), SessionRecordType.SESSION_RECORD_OF_SV, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * This method cannot be annotated with {@link Override} since it has one less parameter.
     */
    @RequestMapping(value = SvUrlParts.NEW_URL, method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {
        return super.initForm(SessionRecordType.SESSION_RECORD_OF_SV, request, model);
    }

    @Override
    @RequestMapping(value = SvUrlParts.NEW_URL, method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute(AbstractNewRecordController.COMMAND) NewSessionRecord command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
