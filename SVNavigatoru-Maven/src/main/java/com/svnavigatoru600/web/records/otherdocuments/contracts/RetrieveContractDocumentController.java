package com.svnavigatoru600.web.records.otherdocuments.contracts;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.otherdocuments.AbstractRetrieveDocumentController;
import com.svnavigatoru600.web.url.records.RecordsCommonUrlParts;
import com.svnavigatoru600.web.url.records.otherdocuments.ContractsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveContractDocumentController extends AbstractRetrieveDocumentController {

    @Inject
    public RetrieveContractDocumentController(final OtherDocumentRecordService recordService,
            final MessageSource messageSource) {
        super(ContractsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordTypeEnum.CONTRACT, recordService,
                messageSource);
    }

    @Override
    @GetMapping(value = ContractsUrlParts.EXISTING_URL + "{recordId}/" + RecordsCommonUrlParts.DOWNLOAD_EXTENSION)
    public void retrieve(@PathVariable final int recordId, final HttpServletResponse response, final ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
