package com.svnavigatoru600.web.records.otherdocuments.accounting;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.otherdocuments.AccountingUrlParts;
import com.svnavigatoru600.viewmodel.records.otherdocuments.NewRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.NewRecordValidator;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;
import com.svnavigatoru600.web.records.otherdocuments.AbstractNewDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewAccountingDocumentController extends AbstractNewDocumentController {

    @Inject
    public NewAccountingDocumentController(final OtherDocumentRecordService recordService,
            final SendNotificationNewModelFiller sendNotificationModelFiller, final NewRecordValidator validator,
            final MessageSource messageSource) {
        super(AccountingUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordType.ACCOUNTING, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    @Override
    @GetMapping(value = AccountingUrlParts.NEW_URL)
    public String initForm(final HttpServletRequest request, final ModelMap model) {
        return super.initForm(request, model);
    }

    @Override
    @PostMapping(value = AccountingUrlParts.NEW_URL)
    public String processSubmittedForm(@ModelAttribute(AbstractNewDocumentController.COMMAND) final NewRecord command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
