package com.svnavigatoru600.web.records.otherdocuments.revisions;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.url.records.otherdocuments.RevisionsUrlParts;
import com.svnavigatoru600.web.records.otherdocuments.AbstractListDocumentsController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListRevisionDocumentsController extends AbstractListDocumentsController {

    @Inject
    public ListRevisionDocumentsController(final OtherDocumentRecordService recordService,
            final MessageSource messageSource) {
        super(RevisionsUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordTypeEnum.REGULAR_REVISION, recordService,
                messageSource);
    }

    @Override
    @GetMapping(value = RevisionsUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @GetMapping(value = RevisionsUrlParts.CREATED_URL)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @GetMapping(value = RevisionsUrlParts.DELETED_URL)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
