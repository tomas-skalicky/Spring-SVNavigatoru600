package com.svnavigatoru600.web.records.session.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.web.records.session.AbstractRetrieveRecordController;
import com.svnavigatoru600.web.url.records.RecordsCommonUrlParts;
import com.svnavigatoru600.web.url.records.session.AllSessionsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveAllSessionRecordController extends AbstractRetrieveRecordController {

    @Inject
    public RetrieveAllSessionRecordController(final SessionRecordService recordService,
            final MessageSource messageSource) {
        super(AllSessionsUrlParts.BASE_URL, new PageViews(), recordService, messageSource);
    }

    @Override
    @GetMapping(value = AllSessionsUrlParts.EXISTING_URL + "{recordId}/" + RecordsCommonUrlParts.DOWNLOAD_EXTENSION)
    public void retrieve(@PathVariable final int recordId, final HttpServletResponse response, final ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
