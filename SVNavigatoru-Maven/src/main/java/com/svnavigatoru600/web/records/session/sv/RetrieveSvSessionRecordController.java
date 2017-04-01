package com.svnavigatoru600.web.records.session.sv;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.records.RecordsCommonUrlParts;
import com.svnavigatoru600.url.records.session.SvUrlParts;
import com.svnavigatoru600.web.records.session.AbstractRetrieveRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveSvSessionRecordController extends AbstractRetrieveRecordController {

    /**
     * Constructor.
     */
    @Inject
    public RetrieveSvSessionRecordController(final SessionRecordService recordService, final MessageSource messageSource) {
        super(SvUrlParts.BASE_URL, new PageViews(), SessionRecordType.SESSION_RECORD_OF_SV, recordService,
                messageSource);
    }

    @Override
    @RequestMapping(value = SvUrlParts.EXISTING_URL + "{recordId}/"
            + RecordsCommonUrlParts.DOWNLOAD_EXTENSION, method = RequestMethod.GET)
    public void retrieve(@PathVariable final int recordId, final HttpServletResponse response, final ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
