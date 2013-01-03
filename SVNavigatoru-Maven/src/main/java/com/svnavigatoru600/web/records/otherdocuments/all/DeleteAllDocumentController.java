package com.svnavigatoru600.web.records.otherdocuments.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.service.records.otherdocuments.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.otherdocuments.AbstractDeleteDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteAllDocumentController extends AbstractDeleteDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/";

    /**
     * Constructor.
     */
    @Inject
    public DeleteAllDocumentController(OtherDocumentRecordService recordService, MessageSource messageSource) {
        super(DeleteAllDocumentController.BASE_URL, new PageViews(), recordService, messageSource);
    }

    @Override
    @RequestMapping(value = DeleteAllDocumentController.BASE_URL + "existujici/{recordId}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
