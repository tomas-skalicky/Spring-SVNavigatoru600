package com.svnavigatoru600.web.records.session.sv;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.web.records.session.AbstractDeleteRecordController;
import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.records.session.SvUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteSvSessionRecordController extends AbstractDeleteRecordController {

    @Inject
    public DeleteSvSessionRecordController(final SessionRecordService recordService, final MessageSource messageSource) {
        super(SvUrlParts.BASE_URL, new PageViews(), SessionRecordTypeEnum.SESSION_RECORD_OF_SV, recordService,
                messageSource);
    }

    @Override
    @GetMapping(value = SvUrlParts.EXISTING_URL + "{recordId}/" + CommonUrlParts.DELETE_EXTENSION)
    public String delete(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
