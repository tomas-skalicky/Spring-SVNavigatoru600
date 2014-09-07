package com.svnavigatoru600.web.records.otherdocuments.revisions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.otherdocuments.RevisionsUrlParts;
import com.svnavigatoru600.web.records.otherdocuments.AbstractListDocumentsController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListRevisionDocumentsController extends AbstractListDocumentsController {

    /**
     * Constructor.
     */
    @Inject
    public ListRevisionDocumentsController(OtherDocumentRecordService recordService,
            MessageSource messageSource) {
        super(RevisionsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordType.REGULAR_REVISION,
                recordService, messageSource);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.CREATED_URL, method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @RequestMapping(value = RevisionsUrlParts.DELETED_URL, method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
