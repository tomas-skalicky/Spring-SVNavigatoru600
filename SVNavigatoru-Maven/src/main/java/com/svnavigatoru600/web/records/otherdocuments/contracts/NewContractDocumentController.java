package com.svnavigatoru600.web.records.otherdocuments.contracts;

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

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.otherdocuments.ContractsUrlParts;
import com.svnavigatoru600.viewmodel.records.otherdocuments.NewRecord;
import com.svnavigatoru600.viewmodel.records.otherdocuments.validator.NewRecordValidator;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;
import com.svnavigatoru600.web.records.otherdocuments.AbstractNewDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewContractDocumentController extends AbstractNewDocumentController {

    /**
     * Constructor.
     */
    @Inject
    public NewContractDocumentController(OtherDocumentRecordService recordService,
            SendNotificationNewModelFiller sendNotificationModelFiller, NewRecordValidator validator,
            MessageSource messageSource) {
        super(ContractsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordType.CONTRACT, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    @Override
    @RequestMapping(value = ContractsUrlParts.NEW_URL, method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {
        return super.initForm(request, model);
    }

    @Override
    @RequestMapping(value = ContractsUrlParts.NEW_URL, method = RequestMethod.POST)
    public String processSubmittedForm(
            @ModelAttribute(AbstractNewDocumentController.COMMAND) NewRecord command, BindingResult result,
            SessionStatus status, HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
