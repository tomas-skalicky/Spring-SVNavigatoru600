package com.svnavigatoru600.web.records.otherdocuments.revisions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.records.otherdocuments.AbstractRetrieveDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveRevisionDocumentController extends AbstractRetrieveDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/pravidelne-revize/";

    /**
     * Constructor.
     */
    @Inject
    public RetrieveRevisionDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(RetrieveRevisionDocumentController.BASE_URL, new PageViews(),
                OtherDocumentRecordType.REGULAR_REVISION, recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = RetrieveRevisionDocumentController.BASE_URL + "existujici/{recordId}/stahnout/", method = RequestMethod.GET)
    public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
