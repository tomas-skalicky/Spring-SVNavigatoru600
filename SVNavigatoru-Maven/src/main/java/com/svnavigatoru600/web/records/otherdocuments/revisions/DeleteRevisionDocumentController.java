package com.svnavigatoru600.web.records.otherdocuments.revisions;

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
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.records.otherdocuments.RevisionsUrlParts;
import com.svnavigatoru600.web.records.otherdocuments.AbstractDeleteDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteRevisionDocumentController extends AbstractDeleteDocumentController {

    /**
     * Constructor.
     */
    @Inject
    public DeleteRevisionDocumentController(OtherDocumentRecordService recordService,
            MessageSource messageSource) {
        super(RevisionsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordType.REGULAR_REVISION,
                recordService, messageSource);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.EXISTING_URL + "{recordId}/" + CommonUrlParts.DELETE_EXTENSION, method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
