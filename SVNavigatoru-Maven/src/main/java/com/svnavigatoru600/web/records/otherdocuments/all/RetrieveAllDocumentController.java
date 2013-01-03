package com.svnavigatoru600.web.records.otherdocuments.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.service.records.otherdocuments.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.otherdocuments.AbstractRetrieveDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveAllDocumentController extends AbstractRetrieveDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/";

    /**
     * Constructor.
     */
    @Inject
    public RetrieveAllDocumentController(OtherDocumentRecordService recordService, MessageSource messageSource) {
        super(RetrieveAllDocumentController.BASE_URL, new PageViews(), recordService, messageSource);
    }

    @Override
    @RequestMapping(value = RetrieveAllDocumentController.BASE_URL + "existujici/{recordId}/stahnout/", method = RequestMethod.GET)
    public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
