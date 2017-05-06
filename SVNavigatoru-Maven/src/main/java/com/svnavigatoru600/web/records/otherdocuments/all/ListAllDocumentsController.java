package com.svnavigatoru600.web.records.otherdocuments.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.otherdocuments.AbstractListDocumentsController;
import com.svnavigatoru600.web.url.records.otherdocuments.AllDocumentsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListAllDocumentsController extends AbstractListDocumentsController {

    @Inject
    public ListAllDocumentsController(final OtherDocumentRecordService recordService,
            final MessageSource messageSource) {
        super(AllDocumentsUrlParts.BASE_URL, new PageViews(), recordService, messageSource);
    }

    @Override
    @GetMapping(value = AllDocumentsUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @GetMapping(value = AllDocumentsUrlParts.CREATED_URL)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @GetMapping(value = AllDocumentsUrlParts.DELETED_URL)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
