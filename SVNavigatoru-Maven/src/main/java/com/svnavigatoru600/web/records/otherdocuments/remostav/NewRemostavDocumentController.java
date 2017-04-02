package com.svnavigatoru600.web.records.otherdocuments.remostav;

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

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.otherdocuments.RemostavDocumentsUrlParts;
import com.svnavigatoru600.viewmodel.records.otherdocuments.NewRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.NewRecordValidator;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;
import com.svnavigatoru600.web.records.otherdocuments.AbstractNewDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewRemostavDocumentController extends AbstractNewDocumentController {

    @Inject
    public NewRemostavDocumentController(final OtherDocumentRecordService recordService,
            final SendNotificationNewModelFiller sendNotificationModelFiller, final NewRecordValidator validator,
            final MessageSource messageSource) {
        super(RemostavDocumentsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordTypeEnum.REMOSTAV, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    @Override
    @GetMapping(value = RemostavDocumentsUrlParts.NEW_URL)
    public String initForm(final HttpServletRequest request, final ModelMap model) {
        return super.initForm(request, model);
    }

    @Override
    @PostMapping(value = RemostavDocumentsUrlParts.NEW_URL)
    public String processSubmittedForm(@ModelAttribute(AbstractNewDocumentController.COMMAND) final NewRecord command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request,
            final ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
