package com.svnavigatoru600.web.records.otherdocuments.others;

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
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.records.otherdocuments.validator.NewRecordValidator;
import com.svnavigatoru600.viewmodel.records.otherdocuments.NewRecord;
import com.svnavigatoru600.web.records.otherdocuments.AbstractNewDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewOtherDocumentController extends AbstractNewDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/ostatni/";

    /**
     * Constructor.
     */
    @Inject
    public NewOtherDocumentController(OtherDocumentRecordDao recordDao, NewRecordValidator validator,
            MessageSource messageSource) {
        super(NewOtherDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.OTHER, recordDao,
                validator, messageSource);
    }

    @Override
    @RequestMapping(value = NewOtherDocumentController.BASE_URL + "novy/", method = RequestMethod.GET)
    public String initForm(HttpServletRequest request, ModelMap model) {
        return super.initForm(request, model);
    }

    @Override
    @RequestMapping(value = NewOtherDocumentController.BASE_URL + "novy/", method = RequestMethod.POST)
    public String processSubmittedForm(@ModelAttribute(AbstractNewDocumentController.COMMAND) NewRecord command,
            BindingResult result, SessionStatus status, HttpServletRequest request, ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
