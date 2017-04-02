package com.svnavigatoru600.web.records.session.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.CommonUrlParts;
import com.svnavigatoru600.url.records.session.AllSessionsUrlParts;
import com.svnavigatoru600.web.records.session.AbstractDeleteRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteAllSessionRecordController extends AbstractDeleteRecordController {

    @Inject
    public DeleteAllSessionRecordController(final SessionRecordService recordService, final MessageSource messageSource) {
        super(AllSessionsUrlParts.BASE_URL, new PageViews(), recordService, messageSource);
    }

    @Override
    @GetMapping(value = AllSessionsUrlParts.EXISTING_URL + "{recordId}/"
            + CommonUrlParts.DELETE_EXTENSION)
    public String delete(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
