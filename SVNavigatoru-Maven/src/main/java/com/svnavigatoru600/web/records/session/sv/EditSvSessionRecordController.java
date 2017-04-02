package com.svnavigatoru600.web.records.session.sv;

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

import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.records.session.SvUrlParts;
import com.svnavigatoru600.viewmodel.records.session.EditSessionRecord;
import com.svnavigatoru600.viewmodel.records.session.validator.EditSessionRecordValidator;
import com.svnavigatoru600.web.SendNotificationEditModelFiller;
import com.svnavigatoru600.web.records.session.AbstractEditRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class EditSvSessionRecordController extends AbstractEditRecordController {

    @Inject
    public EditSvSessionRecordController(final SessionRecordService recordService,
            final SendNotificationEditModelFiller sendNotificationModelFiller, final EditSessionRecordValidator validator,
            final MessageSource messageSource) {
        super(SvUrlParts.EXISTING_URL, new PageViews(), SessionRecordTypeEnum.SESSION_RECORD_OF_SV, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    @Override
    @GetMapping(value = SvUrlParts.EXISTING_URL + "{recordId}/")
    public String initForm(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.initForm(recordId, request, model);
    }

    @Override
    @GetMapping(value = SvUrlParts.EXISTING_URL + "{recordId}/" + CommonUrlParts.SAVED_EXTENSION)
    public String initFormAfterSave(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.initFormAfterSave(recordId, request, model);
    }

    @Override
    @PostMapping(value = SvUrlParts.EXISTING_URL + "{recordId}/")
    public String processSubmittedForm(@ModelAttribute(AbstractEditRecordController.COMMAND) final EditSessionRecord command,
            final BindingResult result, final SessionStatus status, @PathVariable final int recordId, final HttpServletRequest request,
            final ModelMap model) {
        return super.processSubmittedForm(command, result, status, recordId, request, model);
    }
}
