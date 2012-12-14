package com.svnavigatoru600.web.records.otherdocuments.accounting;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.records.otherdocuments.ListDocumentsController;

@Controller
public class ListAccountingDocumentsController extends ListDocumentsController {

    private static final String BASE_URL = "/dalsi-dokumenty/ucetnictvi/";

    /**
     * Constructor.
     */
    @Autowired
    public ListAccountingDocumentsController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(ListAccountingDocumentsController.BASE_URL, new PageViews(),
                OtherDocumentRecordType.ACCOUNTING, recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = ListAccountingDocumentsController.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @RequestMapping(value = ListAccountingDocumentsController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @RequestMapping(value = ListAccountingDocumentsController.BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
