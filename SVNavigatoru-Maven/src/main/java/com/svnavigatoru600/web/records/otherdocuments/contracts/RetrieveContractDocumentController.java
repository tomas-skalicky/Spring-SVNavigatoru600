package com.svnavigatoru600.web.records.otherdocuments.contracts;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.RecordsCommonUrlParts;
import com.svnavigatoru600.url.records.otherdocuments.ContractsUrlParts;
import com.svnavigatoru600.web.records.otherdocuments.AbstractRetrieveDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveContractDocumentController extends AbstractRetrieveDocumentController {

    /**
     * Constructor.
     */
    @Inject
    public RetrieveContractDocumentController(OtherDocumentRecordService recordService,
            MessageSource messageSource) {
        super(ContractsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordType.CONTRACT, recordService,
                messageSource);
    }

    @Override
    @RequestMapping(value = ContractsUrlParts.EXISTING_URL + "{recordId}/"
            + RecordsCommonUrlParts.DOWNLOAD_EXTENSION, method = RequestMethod.GET)
    public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
