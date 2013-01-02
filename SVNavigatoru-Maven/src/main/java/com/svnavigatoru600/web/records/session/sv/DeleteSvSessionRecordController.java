package com.svnavigatoru600.web.records.session.sv;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.session.SessionRecordService;
import com.svnavigatoru600.web.records.session.AbstractDeleteRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteSvSessionRecordController extends AbstractDeleteRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/sv/";

    /**
     * Constructor.
     */
    @Inject
    public DeleteSvSessionRecordController(SessionRecordService recordService, MessageSource messageSource) {
        super(DeleteSvSessionRecordController.BASE_URL, new PageViews(),
                SessionRecordType.SESSION_RECORD_OF_SV, recordService, messageSource);
    }

    @Override
    @RequestMapping(value = DeleteSvSessionRecordController.BASE_URL + "existujici/{recordId}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
