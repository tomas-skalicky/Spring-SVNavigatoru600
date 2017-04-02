package com.svnavigatoru600.web.records.session.board;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.svnavigatoru600.domain.records.SessionRecordTypeEnum;
import com.svnavigatoru600.service.records.SessionRecordService;
import com.svnavigatoru600.url.records.session.BoardSessionsUrlParts;
import com.svnavigatoru600.viewmodel.records.session.NewSessionRecord;
import com.svnavigatoru600.viewmodel.records.session.validator.NewSessionRecordValidator;
import com.svnavigatoru600.web.SendNotificationNewModelFiller;
import com.svnavigatoru600.web.records.session.AbstractNewRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class NewBoardSessionRecordController extends AbstractNewRecordController {

    @Inject
    public NewBoardSessionRecordController(final SessionRecordService recordService,
            final SendNotificationNewModelFiller sendNotificationModelFiller, final NewSessionRecordValidator validator,
            final MessageSource messageSource) {
        super(BoardSessionsUrlParts.BASE_URL, new PageViews(), SessionRecordTypeEnum.SESSION_RECORD_OF_BOARD, recordService,
                sendNotificationModelFiller, validator, messageSource);
    }

    /**
     * This method cannot be annotated with {@link Override} since it has one less parameter.
     */
    @GetMapping(value = BoardSessionsUrlParts.NEW_URL)
    public String initForm(final HttpServletRequest request, final ModelMap model) {
        return super.initForm(SessionRecordTypeEnum.SESSION_RECORD_OF_BOARD, request, model);
    }

    @Override
    @PostMapping(value = BoardSessionsUrlParts.NEW_URL)
    public String processSubmittedForm(@ModelAttribute(AbstractNewRecordController.COMMAND) final NewSessionRecord command,
            final BindingResult result, final SessionStatus status, final HttpServletRequest request, final ModelMap model) {
        return super.processSubmittedForm(command, result, status, request, model);
    }
}
