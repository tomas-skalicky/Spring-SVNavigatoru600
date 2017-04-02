package com.svnavigatoru600.web.records.otherdocuments.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.RecordsCommonUrlParts;
import com.svnavigatoru600.url.records.otherdocuments.AllDocumentsUrlParts;
import com.svnavigatoru600.web.records.otherdocuments.AbstractRetrieveDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveAllDocumentController extends AbstractRetrieveDocumentController {

    @Inject
    public RetrieveAllDocumentController(final OtherDocumentRecordService recordService,
            final MessageSource messageSource) {
        super(AllDocumentsUrlParts.BASE_URL, new PageViews(), recordService, messageSource);
    }

    @Override
    @GetMapping(value = AllDocumentsUrlParts.EXISTING_URL + "{recordId}/" + RecordsCommonUrlParts.DOWNLOAD_EXTENSION)
    public void retrieve(@PathVariable final int recordId, final HttpServletResponse response, final ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
