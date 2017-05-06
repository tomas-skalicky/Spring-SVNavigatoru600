package com.svnavigatoru600.web.records.session.board;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.web.records.session.AbstractListRecordsController;
import com.svnavigatoru600.web.url.records.session.BoardSessionsUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListBoardSessionRecordsController extends AbstractListRecordsController {

    @Inject
    public ListBoardSessionRecordsController(final SessionRecordService recordService,
            final MessageSource messageSource) {
        super(BoardSessionsUrlParts.BASE_URL, new PageViews(), SessionRecordTypeEnum.SESSION_RECORD_OF_BOARD, recordService,
                messageSource);
    }

    @Override
    @GetMapping(value = BoardSessionsUrlParts.BASE_URL)
    public String initPage(final HttpServletRequest request, final ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @GetMapping(value = BoardSessionsUrlParts.CREATED_URL)
    public String initPageAfterCreate(final HttpServletRequest request, final ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @GetMapping(value = BoardSessionsUrlParts.DELETED_URL)
    public String initPageAfterDelete(final HttpServletRequest request, final ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
