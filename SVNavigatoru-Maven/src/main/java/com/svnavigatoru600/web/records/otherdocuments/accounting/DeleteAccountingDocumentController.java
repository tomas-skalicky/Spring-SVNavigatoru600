package com.svnavigatoru600.web.records.otherdocuments.accounting;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.service.records.OtherDocumentRecordService;
import com.svnavigatoru600.web.records.otherdocuments.AbstractDeleteDocumentController;
import com.svnavigatoru600.web.url.CommonUrlParts;
import com.svnavigatoru600.web.url.records.otherdocuments.AccountingUrlParts;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteAccountingDocumentController extends AbstractDeleteDocumentController {

    @Inject
    public DeleteAccountingDocumentController(final OtherDocumentRecordService recordService,
            final MessageSource messageSource) {
        super(AccountingUrlParts.BASE_URL, new PageViews(), OtherDocumentRecordTypeEnum.ACCOUNTING, recordService,
                messageSource);
    }

    @Override
    @GetMapping(value = AccountingUrlParts.EXISTING_URL + "{recordId}/" + CommonUrlParts.DELETE_EXTENSION)
    public String delete(@PathVariable final int recordId, final HttpServletRequest request, final ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
