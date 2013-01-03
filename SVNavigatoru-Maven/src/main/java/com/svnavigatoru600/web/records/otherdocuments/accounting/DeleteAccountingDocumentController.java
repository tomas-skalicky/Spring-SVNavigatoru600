package com.svnavigatoru600.web.records.otherdocuments.accounting;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.otherdocuments.AbstractDeleteDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteAccountingDocumentController extends AbstractDeleteDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/ucetnictvi/";

    /**
     * Constructor.
     */
    @Inject
    public DeleteAccountingDocumentController(OtherDocumentRecordService recordService,
            MessageSource messageSource) {
        super(DeleteAccountingDocumentController.BASE_URL, new PageViews(),
                OtherDocumentRecordType.ACCOUNTING, recordService, messageSource);
    }

    @Override
    @RequestMapping(value = DeleteAccountingDocumentController.BASE_URL + "existujici/{recordId}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
