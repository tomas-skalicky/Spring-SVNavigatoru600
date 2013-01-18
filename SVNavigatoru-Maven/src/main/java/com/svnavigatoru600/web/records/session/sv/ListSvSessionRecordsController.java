package com.svnavigatoru600.web.records.session.sv;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.records.session.SvUrlParts;
import com.svnavigatoru600.web.records.session.AbstractListRecordsController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListSvSessionRecordsController extends AbstractListRecordsController {

    /**
     * Constructor.
     */
    @Inject
    public ListSvSessionRecordsController(SessionRecordService recordService, MessageSource messageSource) {
        super(SvUrlParts.BASE_URL, new PageViews(), SessionRecordType.SESSION_RECORD_OF_SV, recordService,
                messageSource);
    }

    @Override
    @RequestMapping(value = SvUrlParts.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @RequestMapping(value = SvUrlParts.CREATED_URL, method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @RequestMapping(value = SvUrlParts.DELETED_URL, method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
