package com.svnavigatoru600.web.records.otherdocuments.others;

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
import com.svnavigatoru600.url.records.otherdocuments.OthersUrlParts;
import com.svnavigatoru600.web.records.otherdocuments.AbstractRetrieveDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveOtherDocumentController extends AbstractRetrieveDocumentController {

    /**
     * Constructor.
     */
    @Inject
    public RetrieveOtherDocumentController(final OtherDocumentRecordService recordService, final MessageSource messageSource) {
        super(OthersUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordType.OTHER, recordService, messageSource);
    }

    @Override
    @RequestMapping(value = OthersUrlParts.EXISTING_URL + "{recordId}/"
            + RecordsCommonUrlParts.DOWNLOAD_EXTENSION, method = RequestMethod.GET)
    public void retrieve(@PathVariable final int recordId, final HttpServletResponse response, final ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
